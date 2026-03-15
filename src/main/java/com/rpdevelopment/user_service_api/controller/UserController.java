package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    // =============== DEPENDÊNCIAS ====================

    private final UserService userService;


    // ========= CONSTRUTOR DEPENDÊNCIAS ===============

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // =============== GET ====================

    //USUÁRIO LOGADO
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/me")
    public ResponseEntity<UserPersonAddressDto> findMe() {
        UserPersonAddressDto dto = userService.getMe();
        return ResponseEntity.ok(dto);
    }


    // =============== PUT ====================

    //UPDATE USER/ROLE
    @PutMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        userService.addRoleToUser(userId, roleId);

        return ResponseEntity.noContent().build();
    }


    // =============== DELETE ====================

    //DELETE USER/ROLE
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        userService.removeRoleToUser(userId, roleId);

        return ResponseEntity.noContent().build();
    }
}
