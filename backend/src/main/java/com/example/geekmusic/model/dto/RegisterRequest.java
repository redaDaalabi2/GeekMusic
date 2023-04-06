package com.example.geekmusic.model.dto;

import com.example.geekmusic.model.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Getter
@Setter
public class RegisterRequest {
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @JsonIgnore
    private Boolean isEnabled = false;
    @JsonIgnore
    private Collection<Role> roles = new ArrayList<>();
    @JsonIgnore
    private final LocalDateTime createdAt = LocalDateTime.now();
    @JsonIgnore
    private final LocalDateTime updated_at = LocalDateTime.now();


}
