package com.example.geekmusic.api;

import com.example.geekmusic.config.JwtUtil;
import com.example.geekmusic.model.dto.AuthenticationRequest;
import com.example.geekmusic.model.dto.RegisterRequest;
import com.example.geekmusic.model.dto.TokenResponse;
import com.example.geekmusic.model.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
//    @Qualifier("auteurDetailsService")
//    private final AuteurDoctorDetailsService auteurDetailsService;
//    @Qualifier("auditeurDetailsService")
//    private final AuditeurDetailsService auditeurDetailsService;
    private final JwtUtil jwtUtil;
    private final RegisterService registerService;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate( @RequestBody AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim()+"-AUDITEUR",
                        request.getPassword().trim())
        );

        final UserDetails user = userDetailsService.loadUserByUsername(request.getEmail()+"-AUDITEUR");
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(jwtUtil.generateToken(user));
            return ResponseEntity.ok(tokenResponse);

    }

    @PostMapping("/auteurAuth")
    public ResponseEntity authenticateDoctor( @RequestBody AuthenticationRequest request) {
        System.out.println("AUTEUR AUTH");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim()+"-AUTEUR", request.getPassword())
        );

        final UserDetails user = userDetailsService.loadUserByUsername(request.getEmail()+"-AUTEUR");
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(jwtUtil.generateToken(user));

        return ResponseEntity.ok(tokenResponse);

    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        return registerService.register(registerRequest);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registerService.confirmToken(token);
    }
}
