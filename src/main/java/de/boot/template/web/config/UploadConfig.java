package de.boot.template.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@Configuration
public class UploadConfig {

        public final static String UPLOAD_FOLDER = "/uploads";
        public final static String AVATAR_FOLDER = "/avatars";
        public final static String ATTACHMENT_FOLDER = "/attachments";

        @Autowired
        ServletContext servletContext;

        @Bean
        public String getAttachmentPath() {
                return getUploadPath() + ATTACHMENT_FOLDER + File.separator;
        }

        @Bean
        public String getAvatarPath() {
                return getUploadPath() + AVATAR_FOLDER + File.separator;
        }


        @Bean
        public String getUploadPath() {
                return servletContext.getRealPath(UPLOAD_FOLDER);
        }
}
