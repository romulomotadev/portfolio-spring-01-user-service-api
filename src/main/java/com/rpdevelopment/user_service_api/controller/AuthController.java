package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.user.EmailDTO;
import com.rpdevelopment.user_service_api.dto.user.NewPasswordDTO;
import com.rpdevelopment.user_service_api.service.user.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    // =============== POST ====================

    //RECUPERAÇÃO DE SENHA
    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken (@Valid @RequestBody EmailDTO body){
        authService.createRecoverToken(body);
        return ResponseEntity.noContent().build();
    }


    // =============== UPDATE ====================

    //SALVANDO NOVA SENHA
    @PutMapping(value = "/new-password")
    public ResponseEntity<Void> saveNewPassword (@Valid @RequestBody NewPasswordDTO body){
        authService.saveNewPassword(body);
        return ResponseEntity.noContent().build();
    }

}
