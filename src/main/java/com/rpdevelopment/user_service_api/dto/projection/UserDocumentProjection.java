package com.rpdevelopment.user_service_api.dto.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "name", "email", "document" })
@Schema(description = "Projection contendo informações básicas do usuário e seu documento")
public interface UserDocumentProjection {

    //USER
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    String getName();

    @Schema(description = "Email do usuário", example = "joao@email.com")
    String getEmail();

    //PERSON
    @Schema(description = "Número do documento do usuário (CPF ou CNPJ)", example = "12345678900")
    String getDocument();

}
