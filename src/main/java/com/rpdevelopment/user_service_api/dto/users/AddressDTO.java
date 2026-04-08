package com.rpdevelopment.user_service_api.dto.users;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({ "id", "road", "number", "neighborhood", "complement", "city", "zipCode"})
public class AddressDTO {

    //Atributos
    @Schema(description = "Identificador único do endereço gerado pelo banco de dados", example = "1")
    private Long id;
    @NotBlank(message = "Campo rua requerido")
    @Schema(description = "Nome da rua ou logradouro do endereço", example = "Rua das Flores")
    private String road;
    @NotBlank(message = "Campo numero requerido")
    @Schema(description = "Número do imóvel", example = "123")
    private String number;
    @NotBlank(message = "Campo bairro requerido")
    @Schema(description = "Bairro onde o endereço está localizado", example = "Centro")
    private String neighborhood;
    @NotBlank(message = "Campo complemento requerido")
    @Schema(description = "Complemento do endereço, como apartamento, bloco ou casa", example = "Apto 301")
    private String complement;
    @NotBlank(message = "Campo cidade requerido")
    @Schema(description = "Cidade do endereço", example = "Belo Horizonte")
    private String city;
    @NotBlank(message = "Campo cep requerido")
    @Schema(description = "CEP do endereço", example = "30140071")
    private String zipCode;

    //Construtores
    public AddressDTO() {
    }

    public AddressDTO(Long id, String road, String number, String neighborhood, String complement, String city, String zipCode, User user) {
        this.id = id;
        this.road = road;
        this.number = number;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.city = city;
        this.zipCode = zipCode;
    }

    public AddressDTO(Address entity) {
        this.id = entity.getId();
        this.road = entity.getRoad();
        this.number = entity.getNumber();
        this.neighborhood = entity.getNeighborhood();
        this.complement = entity.getComplement();
        this.city = entity.getCity();
        this.zipCode = entity.getZipCode();
    }

    //Getter
    public Long getId() {
        return id;
    }

    public String getRoad() {
        return road;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getComplement() {
        return complement;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

}
