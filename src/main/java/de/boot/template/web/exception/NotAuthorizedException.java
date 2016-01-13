package de.boot.template.web.exception;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
public class NotAuthorizedException extends Exception {

        private String redirectPath;

        public NotAuthorizedException(String redirectPath) {
                this.redirectPath = redirectPath;
        }

        public NotAuthorizedException(String message, String redirectPath) {
                super(message);
                this.redirectPath = redirectPath;
        }

        public String getRedirectPath() {
                return this.redirectPath;
        }
}
