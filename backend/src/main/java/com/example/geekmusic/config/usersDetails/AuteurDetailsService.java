package com.example.geekmusic.config.usersDetails;

import com.example.geekmusic.exception.InvalidCredentialsException;
import com.example.geekmusic.model.entities.Auteur;
import com.example.geekmusic.model.repo.AuteurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("auteurDetailsService")
public class AuteurDetailsService implements UserDetailsService {

    private final AuteurRepo auteurRepo;

    @Autowired
    @Lazy
    public AuteurDetailsService(AuteurRepo auteurRepo) {
        this.auteurRepo = auteurRepo;
    }

    public UserDetailsService auteurDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Auteur user = auteurRepo.getAuteurByEmail(email);
                if (user == null) {
                    throw new InvalidCredentialsException("invalid credentials");
                }
                System.out.println(user);

                return new User(user.getEmail(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("AUTEUR")));

            }
        };

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("IN DOC DETAILS SERVICE");
        Auteur auditeur = auteurRepo.findAuteurByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new User(auditeur.getEmail(), auditeur.getPassword(), Collections.singleton(new SimpleGrantedAuthority("AUTEUR")));
    }
}
