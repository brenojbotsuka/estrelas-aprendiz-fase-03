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

    @Transactional
    public UserResponseDTO criar(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new IllegalArgumentException("Senha inválida. Deve ter pelo menos 8 caracteres.");
        }

        // Criação e população da entidade
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        String senhaCriptografada = passwordEncoder.encode(request.getPassword());
        user.setPassword(senhaCriptografada);

        User userSalvo = userRepository.save(user);

        // Retornar o DTO (sem expor a senha por segurança)
        return new UserResponseDTO(
                userSalvo.getEmail(),
                userSalvo.getPassword(),
                userSalvo.getRole(),
                userSalvo.getId()
        );
    }
}

