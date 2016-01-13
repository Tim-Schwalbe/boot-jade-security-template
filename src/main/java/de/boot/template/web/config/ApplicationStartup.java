package de.boot.template.web.config;


import de.boot.template.web.model.profiles.Role;
import de.boot.template.web.service.ProfileService;
import de.boot.template.web.model.profiles.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  ProfileService profileService;

  @Autowired
  private Environment env;

  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {

    //create first admin profile
    if (profileService.getAllProfiles().size() == 0) {
      String username = env.getProperty("user.first.admin.name");
      String password = env.getProperty("user.first.admin.password");
      String firstName = env.getProperty("user.first.admin.firstname");
      String lastName = env.getProperty("user.first.admin.lastname");
      String email = env.getProperty("user.first.admin.email");

      Profile profile = profileService.createProfile(username, password, firstName, lastName, email, "");

      profile.addRole(Role.ROLE_SUPERADMIN_STRING);
      profile.addRole(Role.ROLE_ADMIN_STRING);
      profile.addRole(Role.ROLE_PROFILE_STRING);
      profile.setEnabled(true);
      profileService.update(profile);

      //create Testuser
      username = "test";
      password = "test";
      firstName = "tim";
      lastName = "schwalbe";
      email = "timschwalbe1@gmail.com";
      profile = profileService.createProfile(username, password, firstName, lastName, email, "");
      profile.addRole(Role.ROLE_PATIENT_STRING);
      profile.addRole(Role.ROLE_PROFILE_STRING);
      profile.setEnabled(true);
      profileService.update(profile);
    }
  }

}
