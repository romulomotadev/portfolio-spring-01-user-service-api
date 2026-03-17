package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "User", description = "Controller for User")
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
    @Operation(
            summary = "Obter usuário logado",
            description = "Retorna os dados do usuário logado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário logado retornados com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido")
            })
    @GetMapping(value = "/me")
    public ResponseEntity<UserPersonAddressDto> findMe() {
        UserPersonAddressDto dto = userService.getMe();
        return ResponseEntity.ok(dto);
    }


    // =============== PUT ====================

    //UPDATE USER/ROLE
    @PutMapping("/{userId}/roles/{roleId}")
    @Operation(
            summary = "Adicionar ou atualizar permissões do usuário",
            description = "Adiciona ou atualiza as permissões associadas a um usuário.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Permissões atualizadas com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "404", description = "Usuário ou permissão não encontrado")
            })
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
    @Operation(
            summary = "Remove permissões do usuário",
            description = "Remove as permissões associadas a um usuário específico.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Permissão removida com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "404", description = "Usuário ou permissão não encontrados")
            })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        userService.removeRoleToUser(userId, roleId);

        return ResponseEntity.noContent().build();
    }
}
