package apiserver.apiserver.security;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService
{
    public boolean isAuthenticatedByPrincipal(final Principal principal, final String username) {
        return principal.getName().equals(username) || this.hasRoleAdmin(principal);
    }
    
    private boolean hasRoleAdmin(final Principal principal) {
        final Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>)((Authentication) principal).getAuthorities();
        return authorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}