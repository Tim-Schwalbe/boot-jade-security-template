package de.boot.template.web.controller;


import de.boot.template.web.helper.AuthHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandingPageController {

  private static final Logger logger = LogManager.getLogger(LandingPageController.class.getName());

  @Autowired
  AuthHelper authHelper;


  @Secured({"ROLE_ADMIN","ROLE_PROFILE", "ROLE_PATIENT", "ROLE_SUPERADMIN", "ROLE_AGENT", "ROLE_SUPERVISOR"})
  @RequestMapping(value = "/landingpage", method = RequestMethod.GET)
  public ModelAndView indexPage() {

    ModelAndView model = new ModelAndView();

    model.setViewName("landingpage/index");

    return model;

  }

  @Secured({"ROLE_ADMIN","ROLE_PROFILE", "ROLE_PATIENT", "ROLE_SUPERADMIN", "ROLE_AGENT", "ROLE_SUPERVISOR"})
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index1Page() {

    ModelAndView model = new ModelAndView();

    model.setViewName("landingpage/index");

    return model;
  }

}
