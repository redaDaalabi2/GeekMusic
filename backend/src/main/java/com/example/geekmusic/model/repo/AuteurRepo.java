package com.example.geekmusic.model.repo;


import com.example.geekmusic.model.entities.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AuteurRepo extends JpaRepository<Auteur, Long> {

    Optional<Auteur> findAuteurByEmail(String email);

    Auteur getAuteurByEmail(String email);
    Auteur findAuteurById(long id);
    Auteur findAuteurByFamilyName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Auteur d SET d.isEnabled = true  WHERE d.email = ?1")
    int enableAuteur(String email);
}
