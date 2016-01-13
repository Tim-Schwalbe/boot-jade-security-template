package de.boot.template.web.controller;


import de.boot.template.web.helper.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ErrorController {

        @Autowired
        AuthHelper authHelper;

        @RequestMapping(value = "/err/{errorCode}", method = RequestMethod.GET)
        public ModelAndView error(@PathVariable int errorCode, RedirectAttributes redirectAttributes) {

                if (errorCode == HttpStatus.NOT_FOUND.value()) {
                        redirectAttributes.addFlashAttribute("notification", "Angeforderte Seite nicht gefunden!");
                } else if (errorCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                        redirectAttributes.addFlashAttribute("notification", "Ein Fehler ist aufgetreten, bitte veruschen Sie es erneut.");
                } else if (errorCode == HttpStatus.FORBIDDEN.value()) {
                        redirectAttributes.addFlashAttribute("notification", "Sie haben keinen Zugriff auf die angefragte Seite.");
                }

                if (authHelper.getCurrentProfile() != null) {
                        return new ModelAndView("redirect:/landingpage");
                }
                else {
                        return new ModelAndView("redirect:/login");
                }
        }
}
