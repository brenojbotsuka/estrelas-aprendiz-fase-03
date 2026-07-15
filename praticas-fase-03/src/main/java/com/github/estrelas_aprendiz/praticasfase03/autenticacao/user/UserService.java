package com.github.estrelas_aprendiz.praticasfase03.autenticacao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findByEmail (String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o email: " + email));
        return user;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        // Criação e população da entidade
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User userSalvo = userRepository.save(user);

        return new UserResponseDTO(
                userSalvo.getEmail(),
                userSalvo.getRole(),
                userSalvo.getId()
        );
    }
}


