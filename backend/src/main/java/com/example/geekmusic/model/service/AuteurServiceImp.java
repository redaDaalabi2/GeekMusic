package com.example.geekmusic.model.service;

import com.example.geekmusic.model.entities.Auteur;
import com.example.geekmusic.model.repo.AuteurRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j


public class AuteurServiceImp implements AuteurService {

    private final AuteurRepo auteur;
    private final ConfirmationTokenService tokenService;

    @Override
    public Auteur saveAuteur(Auteur user) {
        return auteur.save(user);
    }

    @Override
    public Auteur getAuteur(String name) {
        return auteur.findAuteurByFamilyName(name);
    }

    @Override
    public List<Auteur> getAuteurs() {
        return auteur.findAll();
    }

    @Override
    public Auteur findAuteurByEmail(String email) {
        return auteur.getAuteurByEmail(email);
    }

    @Override
    public String signUp(Auteur user) {
        Optional<Auteur> optionalAuteur = auteur.findAuteurByEmail(user.getEmail());
        if ( optionalAuteur.isPresent()) {
            throw new IllegalStateException("email already taken");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        auteur.save(user);
        return null;
    }
}
