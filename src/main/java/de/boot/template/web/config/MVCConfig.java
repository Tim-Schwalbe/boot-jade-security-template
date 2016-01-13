package de.boot.template.web.config;


import de.boot.template.web.interceptors.AuthInterceptor;
import de.boot.template.web.interceptors.CrsfInterceptor;
import de.boot.template.web.interceptors.PathInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * MVC configuration
 * Possibility to add resource handlers, interceptors, view resolvers, etc.
 *
 * @author dennis.wiosna
 */
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

        @Autowired
        CrsfInterceptor crsfInterceptor;
        @Autowired
        AuthInterceptor authInterceptor;
        @Autowired
        PathInterceptor pathInterceptor;
        @Autowired
        LocaleChangeInterceptor localeChangeInterceptor;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**").addResourceLocations("/uploads/");
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(crsfInterceptor);
                registry.addInterceptor(authInterceptor);
                registry.addInterceptor(pathInterceptor);
                registry.addInterceptor(localeChangeInterceptor);
        }
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/landingpage").setViewName("landingpage");
                registry.addViewController("/").setViewName("home");
                registry.addViewController("/hello").setViewName("hello");
                registry.addViewController("/login").setViewName("login");
        }
}
