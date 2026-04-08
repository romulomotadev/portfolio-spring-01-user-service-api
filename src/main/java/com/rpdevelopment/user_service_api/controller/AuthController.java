package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.EmailDTO;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.service.AuthService;
import com.rpdevelopment.user_service_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    // =============== POST ====================

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken (@Valid @RequestBody EmailDTO body){
        authService.createRecoverToken(body);
        return ResponseEntity.noContent().build();
    }

}
