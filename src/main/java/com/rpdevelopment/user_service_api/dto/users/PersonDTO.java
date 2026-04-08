package com.rpdevelopment.user_service_api.dto.users;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({ "id", "document", "type"})
public class PersonDTO {

    //Atributos
    @Schema(description = "Identificador único do documento gerado pelo banco de dados", example = "1")
    private Long id;
    @NotBlank(message = "Campo documento requerido.")
    @Schema(description = "Número do documento do usuário, podendo ser CPF ou CNPJ", example = "12345678900")
    private String document;

    //Atributos associados
    @NotNull(message = "Requerido tipo de documento PF ou PJ.")
    @Schema(description = "Tipo de documento do usuário: PF para pessoa física ou PJ para pessoa jurídica", example = "PF")
    private Type type;

    //Construtores
    public PersonDTO() {
    }

    public PersonDTO(Long id, Type type, String document) {
        this.id = id;
        this.type = type;
        this.document = document;
    }

    public PersonDTO(Person entity) {
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
