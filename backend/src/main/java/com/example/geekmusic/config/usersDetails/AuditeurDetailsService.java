package com.example.geekmusic.config.usersDetails;

import com.example.geekmusic.exception.InvalidCredentialsException;
import com.example.geekmusic.model.entities.AppUser;
import com.example.geekmusic.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("auditeurDetailsService")
public class AuditeurDetailsService implements UserDetailsService {

    private final UserRepo auditeurRepository;

    @Autowired
    @Lazy
    public AuditeurDetailsService(UserRepo auditeurRepository) {
        this.auditeurRepository = auditeurRepository;
    }

    public UserDetailsService auditeurUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                AppUser user = auditeurRepository.getAppUserByEmail(email);
                if (user == null) {
                    throw new InvalidCredentialsException("invalid credentials");
                }
                System.out.println(user);

                return new User(user.getEmail(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("AUDITEUR")));

            }
        };

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = auditeurRepository.getAppUserByEmail(email);
        if (user == null) {
            throw new InvalidCredentialsException("invalid credentials");
        }
        System.out.println("INSIDE AUDITEUR DETAILS SERVICE");
        System.out.println(user);
        return new User(user.getEmail(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("AUDITEUR")));

    }
}
