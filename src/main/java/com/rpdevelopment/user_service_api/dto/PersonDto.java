package com.rpdevelopment.user_service_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.Type;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({ "id", "document", "type"})
public class PersonDto {

    //Atributos
    private Long id;
    @NotBlank(message = "Campo documento requerido.")
    private String document;

    //Atributos associados
    @NotNull(message = "Requerido tipo de documento PF ou PJ.")
    private Type type;

    //Construtores
    public PersonDto() {
    }

    public PersonDto(Long id, Type type, String document) {
        this.id = id;
        this.type = type;
        this.document = document;
    }

    public PersonDto(Person entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.document = entity.getDocument();
    }

    //Getter
    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getDocument() {
        return document;
    }

    //Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(@NotBlank(message = "Campo documento requerido.") String document) {
        this.document = document;
    }

    public void setType(@NotNull(message = "Requerido tipo de documento PF ou PJ.") Type type) {
        this.type = type;
    }
}
