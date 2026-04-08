package com.rpdevelopment.user_service_api.service.user;

import com.rpdevelopment.user_service_api.dto.user.EmailDTO;
import com.rpdevelopment.user_service_api.dto.user.NewPasswordDTO;
import com.rpdevelopment.user_service_api.entity.PasswordRecover;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.exceptions.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.repository.PasswordRecoverRepository;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalTime.now;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired EmailService emailService;

    //RECUPERAÇÃO DE SENHA
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

    //SALVANDO NOVA SENHA
    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {

        //Verifica token valido no banco
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Token invalido");
        }

        //Atualizando senha criptografada
        Optional<User> user = userRepository.findByEmail(result.getFirst().getEmail());
        user.get().setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(user.get());

    }
}
