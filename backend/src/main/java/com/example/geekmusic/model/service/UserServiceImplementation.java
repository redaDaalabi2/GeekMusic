package com.example.geekmusic.model.service;

import com.example.geekmusic.model.entities.AppUser;
import com.example.geekmusic.model.entities.ConfirmationToken;
import com.example.geekmusic.model.entities.Role;
import com.example.geekmusic.model.repo.RoleRepo;
import com.example.geekmusic.model.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImplementation implements UserService{

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final ConfirmationTokenService tokenService;

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("saving new user");
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving new role");
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(long id, String roleName) {
        log.info("adding role to a  user");
        AppUser appUser = userRepo.findAppUserById(id);
        Role role = roleRepo.findByName(roleName);
        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("getting a user by username");
        return userRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("getting list of users");
        return userRepo.findAll();
    }


    public AppUser findUserByEmail(String email) {
        return userRepo.getAppUserByEmail(email);
    }

    @Override
    public String signUp(AppUser user, String role) {
        Optional<AppUser> optionalAppUser = userRepo.findAppUserByEmail(user.getEmail());
        if ( optionalAppUser.isPresent()) {
            throw new IllegalStateException("email already taken");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user = userRepo.save(user);
        addRoleToUser(user.getId(), role);
        log.info("created user  with role");

//        confirm token generate
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
          user
        );
        tokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return userRepo.enableAppUser(email);
    }

}
