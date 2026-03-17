package com.rpdevelopment.user_service_api.dto;

import com.rpdevelopment.user_service_api.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "DTO responsável por representar os perfis de acesso do sistema")
public class RoleDto {

    //ATRIBUTOS
    @Schema(description = "Identificador da role gerado automaticamente pelo banco de dados", example = "1")
    private Long id;
    @Schema(description = "Nome da autoridade/perfil de acesso do usuário", example = "ROLE_ADMIN")
    private String authority;

    //CONSTRUTORES
    public RoleDto() {
    }

    public RoleDto(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDto(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

    //GETTER E SETTER
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
