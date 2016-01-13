package de.boot.template.web.service;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;

import de.boot.template.web.model.profiles.Role;
import de.boot.template.web.model.Repositories.ProfileRepository;
import de.boot.template.web.model.profiles.Profile;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.magicwerk.brownies.collections.BigList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author tim schwalbe
 */
@Repository
@JadeHelper("profileService")
public class ProfileService {

    static Logger logger = Logger.getLogger(ProfileService.class);

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ProfileRepository addressRepository;

    @PersistenceContext
    EntityManager em;

    public synchronized Profile findUserByEmail(String email) {
        Profile user = null;

        try {
            user = profileRepository.findByEmail(email);
        } catch (NoResultException e) {
            logger.warn("No user found with email: " + email);
        }
        return user;
    }

    public synchronized Profile findByUsername(String username) {
        Profile user = null;

        try {

            user = profileRepository.findByUsername(username);
        } catch (NoResultException e) {
            logger.warn("No user found with username: " + username);
        }
        return user;
    }

    //
    @Transactional(readOnly = true)
    public synchronized List<Profile> getAllUsersWithRole(String role) {
        List<Profile> userList = new BigList<>();

        int roleId = Role.roleNameToCode(role);

        if (roleId > 0) {
            javax.persistence.Query query = em.createNamedQuery("Profile.findAllWithRole", Profile.class).setParameter("role", roleId);
            userList.addAll(query.getResultList());
        }

        return userList;
    }

    private boolean isUsernameRegistered(String username) {
        if (findByUsername(username) == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isEmailRegistered(String email) {
        if (findUserByEmail(email) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public synchronized void deleteAddress(Profile profile) {
//        profile.setAddress(null);
//        profileRepository.save(profile);
    }


    public synchronized List<Profile> getAllProfiles() {
        List<Profile> userList = new BigList<>();

        userList = profileRepository.findAll();

        return userList;
    }

    //        }
    @Transactional
    public synchronized Profile findProfileById(Long id) {
        Profile profile = null;

        try {

            profile = profileRepository.getOne(id);
        } catch (NoResultException e) {
            logger.warn("No profile found with id: " + id);
        }
        return profile;
    }

//    @Transactional
//    public synchronized BasisData findBasisDataByProfileId(Long id) {
////        Profile profile = null;
////        try {
////            profile = profileRepository.getOne(id);
////        } catch (NoResultException e) {
////            logger.warn("No profile found with id: " + id);
////        }
////        BasisData basisData = profile.getBasisData();
////        return basisData;
//    }

    @Transactional
    public synchronized Profile createProfile(String username,
                                              String password,
                                              String firstName,
                                              String lastName,
                                              String email,
                                              String uuid,
                                              String surgery) {
        Profile profile = null;

        profile = new Profile(username, password, firstName, lastName, email);
//        profile.setSurgery(surgery);
        profile.setUuid(uuid);
        profileRepository.save(profile);
        //
        //                if (!isEmailRegistered(email) && !isUsernameRegistered(username)) {
        //                        profile = new Profile(username, password, firstName, lastName, email);
        //                        profile.setUuid(uuid);
        //                        em.persist(profile);
        //                }
        return profile;
    }

    @Transactional
    public synchronized Profile createProfile(String username, String password, String firstName, String lastName, String email, String uuid) {
        Profile profile = null;

        profile = new Profile(username, password, firstName, lastName, email);

        profile.setUuid(uuid);
        profileRepository.save(profile);
        //
        //                if (!isEmailRegistered(email) && !isUsernameRegistered(username)) {
        //                        profile = new Profile(username, password, firstName, lastName, email);
        //                        profile.setUuid(uuid);
        //                        em.persist(profile);
        //                }
        return profile;
    }

    //

    @Transactional
    public synchronized void updatePassword(Profile profile, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        if (profile != null && profileRepository.getOne(profile.getId()) != null) {
            if (StringUtils.isNotBlank(hashedPassword)) {
                profile.setPassword(hashedPassword);

                profileRepository.save(profile);
            } else {
                this.logger.warn("Password not specified.");
            }
        } else {
            this.logger.warn("Given Profile does not exist.");
        }
    }

    @Transactional
    public synchronized void remove(Long profileId) {

        Profile profile = profileRepository.getOne(profileId);

        if (profile != null && !profile.getUsername().equals("admin")) {
            profileRepository.delete(profile);
        } else {
            logger.error("Tried to delete user, but given is null or admin");
        }
    }

    //
    @Transactional
    public synchronized void deleteRole(Role role) {
        logger.info("Delete Role " + role);

        if (role != null) {
            logger.info("Role found in DB for user: " + role.getProfile().getUsername() + " and role: " + role.getName());

            //profileRepository.delete(role);
        } else {
            logger.error("Tried to deactivate user, but given is null or admin");
        }
    }
    //
    //                @Transactional(readOnly = true)
    //                public synchronized List<Profile> findAllProfilesByCreationDate(Timestamp createdAt) {
    //                        List<Profile> profiles = null;
    //                        try {
    //                                Query query = em.createNamedQuery("Profile.findAllProfilesByCreationDate", Profile.class);
    //                                profiles = (List<Profile>) query.setParameter("createdAt", createdAt).getResultList();
    //                        } catch (NoResultException e) {
    //                                logger.warn("No profiles found.");
    //                        }
    //                        return profiles;
    //                }
    //
    //                @Transactional(readOnly = true)
    //                public synchronized List<Profile> findAllProfilesByUpdateDate(Timestamp updatedAt) {
    //                        List<Profile> profiles = null;
    //
    //                        try {
    //                                Query query = em.createNamedQuery("Profile.findAllProfilesByUpdateDate", Profile.class);
    //                                profiles = (List<Profile>) query.setParameter("updatedAt", updatedAt).getResultList();
    //                        } catch (NoResultException e) {
    //                                logger.warn("No profiles found.");
    //                        }
    //
    //                        return profiles;
    //                }

    @Transactional
    public synchronized void persist(Profile profile) {
        if (profile != null) {
            profileRepository.save(profile);
        }
    }

    //
    @Transactional
    public synchronized void update(Profile profile) {
        if (profile != null) {
            Profile p = profileRepository.save(profile);
        } else {
            logger.warn("Profile does not exist.");
        }
    }

    //
    @Transactional
    public String passwordResetToken(Profile profile) {
        String resetToken = UUID.randomUUID().toString();

        profile.setPasswordResetToken(resetToken);
        profile.setPasswordResetExpiry(this.getTimestampPlusDays(1));
        profileRepository.save(profile);
        return resetToken;
    }

    //
    private Timestamp getTimestampPlusDays(int daysToAdd) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.DAY_OF_WEEK, daysToAdd);
        timestamp.setTime(cal.getTime().getTime());
        return timestamp;
    }

    //
    @Transactional(readOnly = true)
    public boolean checkOldPassword(Profile profile, String oldPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (profile != null && passwordEncoder.matches(oldPassword, profile.getPassword())) {
            return true;
        }

        return false;
    }

    //

}
