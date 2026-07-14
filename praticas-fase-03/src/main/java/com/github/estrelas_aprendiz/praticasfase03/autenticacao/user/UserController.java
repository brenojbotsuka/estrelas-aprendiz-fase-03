package com.github.estrelas_aprendiz.praticasfase03.autenticacao.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> criar(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.criar(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }
}
