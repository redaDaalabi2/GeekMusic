package com.example.geekmusic.model.dto;

import com.example.geekmusic.model.entities.AppUser;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private static final ModelMapper mapper = new ModelMapper();

    @Test
    public void checkRegisterMapping() {
        RegisterRequest request = new RegisterRequest();
        request.setName("auditeur");
        request.setUsername("auditeur_1");
        request.setEmail("auditeur_1@geekmusic.ma");
        request.setPassword("123456");
        AppUser patient = mapper.map(request, AppUser.class);
        assertEquals(request.getName(), patient.getName());
        assertEquals(request.getUsername(), patient.getUsername());
        assertEquals(request.getEmail(), patient.getEmail());
    }

}