package de.boot.template.web.interceptors;

import de.boot.template.web.exception.NotAuthorizedException;
import de.boot.template.web.model.profiles.Role;
import de.boot.template.web.helper.AuthHelper;

import de.boot.template.web.model.profiles.Profile;
import de.boot.template.web.service.ProfileService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Injects auth information to views
 *
 * @author dennis.wiosna
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(AuthInterceptor.class.getName());

    @Autowired
    ProfileService profileService;

    @Autowired
    AuthHelper authHelper;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && !auth.getPrincipal().equals("anonymousUser")) {
                Profile profile = (Profile) auth.getPrincipal();

                if (!profile.hasRole(Role.ROLE_ADMIN_STRING) && !profile.hasRole(Role.ROLE_SUPERADMIN_STRING)) {
                    Hibernate.initialize(profile);

                    Map<String, String> pathVariables = (Map<String, String>) request
                            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                    Map<String, String[]> requestParams = request.getParameterMap();

                    Map<String, String> mergedParams = new HashMap<>();
                    mergedParams.putAll(pathVariables);

                    for (String key : requestParams.keySet()) {
                        mergedParams.put(key, requestParams.get(key)[0]);
                    }


                    if (mergedParams.containsKey("profileId")) {
                        Long profileId = Long.parseLong(mergedParams.get("profileId"));

                        Profile calledProfile = profileService.findProfileById(profileId);

                        if (!authHelper.getCurrentProfile().equals(calledProfile)) {
                            throw new NotAuthorizedException(
                                    "You are not authorized to work with product: " + calledProfile.getId(), "/tickets");
                        }
                    }

                }
            }
        } catch (NotAuthorizedException nae) {
            HttpSession session = request.getSession(false);
            session.setAttribute("notification", nae.getMessage());
            response.sendRedirect(request.getContextPath() + nae.getRedirectPath());
            logger.error("User not authorized:\n" + nae);
            return false;
        } catch (Exception e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("notification", "Something went wrong. Please try again!");
            response.sendRedirect(request.getContextPath() + "/");
            logger.error(e);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        //if we need auth info
    }
}
