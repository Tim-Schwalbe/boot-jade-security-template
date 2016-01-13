package de.boot.template.web.helper;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import de.boot.template.web.model.profiles.Profile;
import de.boot.template.web.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Authorization helper for views
 *
 * @author dennis.wiosna
 */
@Component
@JadeHelper("authHelper")
public class AuthHelper {

    @Autowired
    ProfileService profileService;

    public boolean isLoggedIn() {
        return getCurrentProfile() != null;
    }

    public boolean hasRole(String role) {
        if (this.isLoggedIn()) {
            Collection<GrantedAuthority> roles = this.getCurrentProfile().getAuthorities();

            for (GrantedAuthority authority : roles) {
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Profile getCurrentProfile() {
        try {

            Profile profile = profileService.findProfileById(((Profile) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getId());
            return profile;

        } catch (Exception e) {
            return null;
        }
    }
}
