package com.example.geekmusic.model.service;

import com.example.geekmusic.model.entities.Auteur;

import java.util.List;

public interface AuteurService {

    Auteur saveAuteur(Auteur user);
    Auteur getAuteur(String username);
    List<Auteur> getAuteurs();
    Auteur findAuteurByEmail(String email);
    String signUp(Auteur user);
}
