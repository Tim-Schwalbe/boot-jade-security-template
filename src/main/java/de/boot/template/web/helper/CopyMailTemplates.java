package de.boot.template.web.helper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author sebastian.weber on 11.11.2015
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@Component
public class CopyMailTemplates {

        private static final Logger logger = LogManager.getLogger(CopyMailTemplates.class.getName());

        private String[] mailTemplateFiles = new String[] { "account_activation.jade", "confirm.jade", "invite.jade", "invoice.jade",
                "password_recovery.jade", "ping.jade", "progress.jade", "reignite.jade", "survey.jade", "ticket_assignee_change.jade",
                "ticket_close.jade", "ticket_creation.jade", "ticket_status_change.jade", "upsell.jade", "welcome.jade" };

        private String emailTemplateDir = null;

        public void copy() {
                File tempDir = com.google.common.io.Files.createTempDir();
                emailTemplateDir = tempDir.getAbsolutePath();

                // just for server log
                logger.info("Path to temp folder: " + emailTemplateDir);

                for (String template : mailTemplateFiles) {
                        try {
                                copyMailTempFile(template);
                        } catch (IOException e) {
                                logger.error(e);
                        }
                }
        }

        private void copyMailTempFile(String templateName) throws IOException {
                InputStream is;
                File tempDir;
                String tempPath = emailTemplateDir;
                is = CopyMailTemplates.class.getResourceAsStream("/mail_templates/" + templateName);

                tempPath = tempPath + "/" + templateName;
                tempDir = new File(tempPath);

                Path path = Files.createFile(tempDir.toPath());

                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);

                is.close();
        }

        public String getEmailTemplateDir() {
                return emailTemplateDir;
        }
}
