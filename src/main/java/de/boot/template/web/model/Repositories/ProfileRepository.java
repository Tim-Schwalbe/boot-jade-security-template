package de.boot.template.web.model.Repositories;


import de.boot.template.web.model.profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by tim on 21.12.2015.
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

        @Transactional
        List<Profile> findByEmailAndLastName(String email, String lastName);
        @Transactional
        void delete(Profile profile);

        @Transactional
        Profile findByUsername(String username);

        @Transactional
        Profile findByEmail(String email);


}
