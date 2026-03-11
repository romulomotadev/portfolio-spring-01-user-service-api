package com.rpdevelopment.user_service_api.dto;

import com.rpdevelopment.user_service_api.entity.Role;

public class RoleDto {

    //ATRIBUTOS
    private Long id;
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
