package de.boot.template.web.helper;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@JadeHelper("_contextPath")
@Component
public class ContextHelper {

        @Autowired
        ServletContext servletContext;

        @Override
        public String toString() {
                return servletContext.getContextPath();
        }
}
