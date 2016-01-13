package de.boot.template.web.helper;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import com.neovisionaries.i18n.CountryCode;

import java.util.Arrays;

/**
 * @author dennis.wiosna
 *         <p>
 *         Copyright 2015 template GmbH, Inc. All rights reserved
 */
@JadeHelper("countryCodes")
public class CountryHelper {

        public CountryCode[] get() {
                //First country code is undefined
                CountryCode[] countryCodes = Arrays.copyOfRange(CountryCode.values(), 1, CountryCode.values().length);

                Arrays.sort(countryCodes, (o1, o2) -> o1.getName().compareTo(o2.getName()));

                return countryCodes;
        }
}
