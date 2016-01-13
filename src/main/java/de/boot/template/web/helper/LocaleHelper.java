package de.boot.template.web.helper;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@JadeHelper("locale")
public class LocaleHelper extends LocaleContextHolder {

}
