package de.boot.template.web.helper;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@JadeHelper("i18n")
@Component
public class I18nHelper extends ReloadableResourceBundleMessageSource {

        public I18nHelper() {
                super();
                String[] resources = { "classpath:message" };
                this.setBasenames(resources);
                this.setDefaultEncoding("UTF-8");
        }
}
