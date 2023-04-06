package com.example.geekmusic.config.usersDetails;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import java.util.List;


@Setter
@Getter
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final List<AuthenticationProvider> providers;


    public CustomAuthenticationProvider(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : providers) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);
            }
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        for (AuthenticationProvider provider : providers) {
            if (provider.supports(authentication)) {
                return true;
            }
        }
        return false;
    }
}