package de.boot.template.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Global app configuration is placed here
 * Initialize your beans and import other configuration classes here
 *
 * @author dennis.wiosna
 */

@SpringBootApplication
@ComponentScan(basePackages = "de.boot.template.web")
public class DemoApplication extends SpringBootServletInitializer {

        private static final Logger logger = LogManager.getLogger(DemoApplication.class.getName());

        private static Class<DemoApplication> applicationClass = DemoApplication.class;

        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
                return application.sources(applicationClass);
        }

        public static void main(String[] args) {
                // Do initializing stuff
                SpringApplication.run(DemoApplication.class, args);
        }
}
