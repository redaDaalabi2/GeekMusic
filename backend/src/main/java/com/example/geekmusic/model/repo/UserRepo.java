package com.example.geekmusic.model.repo;

import com.example.geekmusic.model.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepo extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
    Optional<AppUser> findAppUserByEmail(String email);
    AppUser getAppUserByEmail(String email);
    AppUser findAppUserById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET a.isEnabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}
