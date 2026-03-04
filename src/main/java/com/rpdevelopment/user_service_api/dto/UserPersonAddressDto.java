package com.rpdevelopment.user_service_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"id","name", "email", "birthDate", "password", "person", "addresses"})
public class UserPersonAddressDto {

    //Atributos
    private Long id;
    @NotBlank(message = "Campo nome requerido")
    @Size(min = 6, max = 50, message ="Requerido 6 a 50 caracteres")
    private String name;
    @NotBlank(message = "Campo e-mail requerido")
    @Email(message = "E-mail invalido")
    private String email;
    @NotNull(message = "Campo nascimento obrigatório")
    private LocalDate birthDate;
    @NotBlank(message = "Campo senha requerido")
    @Size(min = 6, max = 12, message = "Requerido de 6 a 12 caracteres.")
    private String password;

    //Atributos associados
    @NotNull
    @Valid
    private PersonDto person;

    @Valid
    private List<AddressDto> addresses = new ArrayList<AddressDto>();

    //Construtores
    public UserPersonAddressDto() {
    }

    public UserPersonAddressDto(Long id, String name, String email, LocalDate birthDate, String password, PersonDto person, List<AddressDto> addresses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
        this.person = person;
        this.addresses = addresses;
    }

    public UserPersonAddressDto(User entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.birthDate = entity.getBirthDate();
        this.password = entity.getPassword();

        this.person = new PersonDto(entity.getPerson());

        for (Address address : entity.getAddresses()) {
            this.addresses.add(new AddressDto(address));
        }
    }

    //Métodos
    public void addAddresses(List<AddressDto> addresses) {
        this.addresses.addAll(addresses);
    }

    //Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPassword() {
        return password;
    }

    public PersonDto getPerson() {
        return person;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    //Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(@NotBlank(message = "Campo nome requerido") @Size(min = 6, max = 50, message = "Requerido 6 a 50 caracteres") String name) {
        this.name = name;
    }

    public void setEmail(@NotBlank(message = "Campo e-mail requerido") @Email(message = "E-mail invalido") String email) {
        this.email = email;
    }

    public void setBirthDate(@NotNull(message = "Campo nascimento obrigatório") LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPassword(@NotBlank(message = "Campo senha requerido") @Size(min = 6, max = 12, message = "Requerido de 6 a 12 caracteres.") String password) {
        this.password = password;
    }

    public void setPerson(@NotNull @Valid PersonDto person) {
        this.person = person;
    }
}
