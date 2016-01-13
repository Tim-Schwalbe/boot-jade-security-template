package de.boot.template.web.interceptors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@Component
public class PathInterceptor extends HandlerInterceptorAdapter {

        private static final Logger logger = LogManager.getLogger(PathInterceptor.class.getName());

        @Override
        public void postHandle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler,
                               final ModelAndView modelAndView) throws Exception {

                if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
                        modelAndView.addObject("currentPath", request.getServletPath());
                }
        }
}
