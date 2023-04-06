package com.example.geekmusic.model.service;


import com.example.geekmusic.model.entities.ConfirmationToken;
import com.example.geekmusic.model.repo.ConfirmationTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo tokenRepo;

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepo.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return tokenRepo.updateConfirmedAt(token, LocalDateTime.now());
    }
}
