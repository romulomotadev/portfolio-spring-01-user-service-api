package com.rpdevelopment.user_service_api.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "name", "email",
        "road", "number", "neighborhood", "complement", "city", "zip_code"})
@Schema(description = "Projection contendo informações do usuário e seu endereço")
public interface UserAddressProjection {

    //USER
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    String getName();

    @Schema(description = "Email do usuário", example = "joao@email.com")
    String getEmail();

    //ADDRESS
    @Schema(description = "Nome da rua ou logradouro do endereço", example = "Rua das Flores")
    String getRoad();
    @Schema(description = "Número do imóvel", example = "123")
    String getNumber();
    @Schema(description = "Bairro do endereço", example = "Centro")
    String getNeighborhood();
    @Schema(description = "Complemento do endereço, como apartamento ou bloco", example = "Apto 301")
    String getComplement();
    @Schema(description = "Cidade do endereço", example = "Belo Horizonte")
    String getCity();
    @Schema(description = "CEP do endereço", example = "30140071")
    String getZipCode();
}
