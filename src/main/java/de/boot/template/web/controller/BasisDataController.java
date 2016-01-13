package de.boot.template.web.controller;


import de.boot.template.web.helper.AuthHelper;
import de.boot.template.web.model.profiles.Profile;
import de.boot.template.web.service.BasisDataService;
import de.boot.template.web.service.ProfileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BasisDataController {

  Logger logger = Logger.getLogger(BasisDataController.class);
  @Autowired
  ProfileService profileService;
  @Autowired
  BasisDataService basisDataService;

  @Autowired
  AuthHelper authHelper;


  @Secured({"ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN", "ROLE_SUPERADMIN"})
  @RequestMapping(value = "/basisdata", method = RequestMethod.GET)

  public ModelAndView showBasisdata() {
    Profile profile = authHelper.getCurrentProfile();

    ModelAndView model = new ModelAndView("basisdata/index");
    model.addObject("profile", profile);


    return model;
  }

  @Secured({"ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN", "ROLE_SUPERADMIN"})
  @RequestMapping(value = "/basisdata/{profileId}/update", method = RequestMethod.POST)
  public ModelAndView updateBasisdata(@PathVariable Long profileId,
                                      @RequestParam(value = "firstName") String firstName, RedirectAttributes redirect) {

    System.out.println("ProfilId im Controller: " + profileId);
    System.out.println("Profilname im Controller: " + firstName);


    ModelAndView model = new ModelAndView("basisdata/show");
    Profile profile = authHelper.getCurrentProfile();
    profile.setFirstName(firstName);
    profileService.update(profile);
    model.addObject("profile", profile);
    return model;
  }

}
