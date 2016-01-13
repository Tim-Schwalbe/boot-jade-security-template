package de.boot.template.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@Configuration
public class I18nConfig {

        @Bean(name = "localeResolver")
        public CookieLocaleResolver getLocaleResolver() {
                CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
                cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
                cookieLocaleResolver.setCookieName("template_sd_pref_lang");
                cookieLocaleResolver.setCookieMaxAge(604800);
                return cookieLocaleResolver;
        }

        @Bean
        public LocaleChangeInterceptor getLocaleChangeInterceptor() {
                LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
                localeChangeInterceptor.setParamName("lang");
                return localeChangeInterceptor;
        }
}
