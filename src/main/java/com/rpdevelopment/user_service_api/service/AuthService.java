package com.rpdevelopment.user_service_api.service;

import com.rpdevelopment.user_service_api.dto.EmailDTO;
import com.rpdevelopment.user_service_api.entity.PasswordRecover;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.repository.PasswordRecoverRepository;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    //valores do properties
    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;
    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO body) {

        //Verifica email presente no banco
        Optional<User> user = userRepository.findByEmail(body.getEmail());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Email not found");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        //Cria token simples UUID
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        entity = passwordRecoverRepository.save(entity);

        //Copo do e-mail
        String text = "Acesse o link para definir uma nova senha\n\n"
                + recoverUri + token + ". Validade de " + tokenMinutes + " minutos";

        //Envio do e-mail
        emailService.sendEmail(body.getEmail(), "recuperação de senha", text);
    }
}
