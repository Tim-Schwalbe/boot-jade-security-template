package de.boot.template.web.helper;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
public class DataObject {

        private long value;
        private String text;

        public DataObject(long value, String text) {
                this.value = value;
                this.text = text;
        }

        public String toString() {
                return "{value: " + this.value + ", text:\"" + this.text + "\"}";
        }
}
