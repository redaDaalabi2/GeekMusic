package com.example.geekmusic.model.service;

import com.example.geekmusic.helper.email.EmailSender;
import com.example.geekmusic.helper.email.EmailValidator;
import com.example.geekmusic.model.dto.RegisterRequest;
import com.example.geekmusic.model.entities.AppUser;
import com.example.geekmusic.model.entities.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@Slf4j
public class RegisterService {
    private final EmailValidator emailValidator;
    private final ModelMapper mapper = new ModelMapper();
    private UserServiceImplementation userService;
    private  ConfirmationTokenService confirmationTokenService;
    private final UserServiceImplementation userServiceImplementation;
    private final EmailSender emailSender;

    public String register(RegisterRequest request) {
         String CONFIRMATION_LINK = "http://localhost:8080/api/auth/confirm?token=";
        if (!emailValidator.test(request.getEmail())) {
            throw new IllegalStateException("email not valid");
        }
        AppUser user = mapper.map(request, AppUser.class);
        log.info("NEW USER REGISTERED");
       String token = userService.signUp(user, "AUDITEUR");
       emailSender.send(request.getEmail(), emailSender.buildEmail(request.getUsername(),CONFIRMATION_LINK+token) );
       return token;
    }

//    public String Auteurregister(RegisterRequest request) {
//        String CONFIRMATION_LINK = "http://localhost:8080/api/auth/confirm?token=";
//        if (!emailValidator.test(request.getEmail())) {
//            throw new IllegalStateException("email not valid");
//        }
//        Auteur user = mapper.map(request, Auteur.class);
//        log.info("NEW USER REGISTERED");
//        String token = userService.signUp(user, "AUDITEUR");
//        emailSender.send(request.getEmail(), emailSender.buildEmail(request.getUsername(),CONFIRMATION_LINK+token) );
//        return token;
//    }

    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow( () ->
                   new IllegalStateException("token not found")
                );
        if (confirmationToken.getConfirmedAt() != null) {
                throw new IllegalStateException("email already confirmed");
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
               throw new IllegalStateException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        userServiceImplementation.enableAppUser(
                confirmationToken.getUser().getEmail()
        );

        return "confirmed";
    }



}
