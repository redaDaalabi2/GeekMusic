package com.example.geekmusic.api.auteur;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class AuteurController {

    @PostMapping("/docTest")
    public ResponseEntity docTest() {
        return ResponseEntity.ok("Good");
    }
}
