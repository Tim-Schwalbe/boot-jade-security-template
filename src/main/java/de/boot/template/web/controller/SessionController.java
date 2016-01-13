package de.boot.template.web.controller;


import de.boot.template.web.model.profiles.Profile;
import de.boot.template.web.service.ProfileService;
import org.apache.log4j.Logger;
import org.magicwerk.brownies.collections.BigList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class SessionController {

  Logger logger = Logger.getLogger(SessionController.class);
  @Autowired
  ProfileService profileService;


    @Secured({"ROLE_ANONYMOUS"})
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("session/login");
        return model;
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
  }


  @Secured({"ROLE_ANONYMOUS"})
  @RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
  public ModelAndView forgotPasswordPage() {
    return new ModelAndView("session/forgot_password");
  }

  @Secured({ "ROLE_ANONYMOUS" })
  @RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
  public ModelAndView resetPassword(@RequestParam(value = "email") String email, RedirectAttributes redirect) throws MessagingException {

    ModelAndView model = new ModelAndView();
    List<String> notifications = new BigList<>();
    List<String> errors = new BigList<>();
    Profile profile = profileService.findUserByEmail(email);

    if (profile != null) {
      String passwordResetToken = profileService.passwordResetToken(profile);
//      try {
//        mailService.sendPasswordRecovery(profile, passwordResetToken);
//      } catch (MessagingException e) {
//        e.printStackTrace();
//      }
      notifications.add("Password reset instructions got send to your email address!");
      model.setViewName("redirect:/login");
    } else {
      model.setViewName("session/forgot_password");
      errors.add("The email address is wrong!");
    }

    model.addObject("errors", errors);
    redirect.addFlashAttribute("notifications", notifications);
    return model;
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public ModelAndView registerPage() {
    logger.info("Registration Page requested.");
    ModelAndView model = new ModelAndView("session/new");
    model.addObject("newProfile", true);
    return model;
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ModelAndView create(@RequestParam(value = "firstName", required = true) String firstName,
                             @RequestParam(value = "lastName", required = true) String lastName,
                             @RequestParam(value = "username", required = true) String username,
                             @RequestParam(value = "email", required = true) String email,
                             @RequestParam(value = "password", required = true) String password,
                             @RequestParam(value = "password_confirmation", required = true) String password_confirmation,
                             @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                             RedirectAttributes redirect) {

    Profile profile = null;
    List<String> errors = new BigList<>();
    List<String> notifications = new BigList<>();
    ModelAndView model = null;

    logger.info("Jemand wollte sich mit folgenden Daten registrieren:" + " Vorname: " + firstName + " Nachname: " + lastName +
      " Username: " + username + " Email:" + email + " Passwort: " + password);

    String uuid = UUID.randomUUID().toString().replace("-", "");

    if (profileService.findUserByEmail(email) != null) {
      errors.add("Email bereits vergeben!");
    }
    if (profileService.findByUsername(username) != null) {
      errors.add("Benutzername bereits vergeben!");
    }

    profile = profileService.createProfile(username, password, firstName, lastName, email, uuid);
    if (profile != null) {
      model = new ModelAndView("/");
//      try {
//        mailService.sendAccountRegistration(profile);
//      } catch (MessagingException e1) {
//        logger.error("There was a problem problem while sending the registration email: " + e1);
//      }
    } else {
      model = new ModelAndView("redirect:/register");
      errors.add("Der Account konnte nicht erstellt werden!");
    }

    model.addObject("errors", errors);
    redirect.addFlashAttribute("notifications", notifications);

    return model;
  }

  @RequestMapping(value = "/register/verification/{username}/{uuid}", method = RequestMethod.GET)
  public ModelAndView verification(@PathVariable(value = "uuid") String uuid, @PathVariable(value = "username") String username) {
    logger.info("Registration verification Page requested with Username: " + username + " uuid:" + uuid);
    Profile profile = profileService.findByUsername(username);
    ModelAndView model;
    if (profile.getUuid().equals(uuid)) {
      profile.setEnabled(true);
      profileService.persist(profile);
      model = new ModelAndView("/session/success");
    } else {
      model = new ModelAndView("/session/fail");
    }
    return model;
  }

}
