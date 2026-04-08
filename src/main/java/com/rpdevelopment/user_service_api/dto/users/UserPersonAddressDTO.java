package com.rpdevelopment.user_service_api.dto.users;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "DTO responsável por representar o cadastro completo de um usuário, incluindo dados pessoais, documento e endereços.")
@JsonPropertyOrder({"id","name", "email", "birthDate", "password", "person", "addresses"})
public class UserPersonAddressDTO {

    //Atributos
    @Schema(description = "Identificador do usuário gerado automaticamente pelo banco de dados", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    @NotBlank(message = "Campo nome requerido")
    @Size(min = 6, max = 50, message ="Requerido 6 a 50 caracteres")
    private String name;

    @Schema(description = "Endereço de e-mail do usuário", example = "joao@email.com")
    @NotBlank(message = "Campo e-mail requerido")
    @Email(message = "E-mail invalido")
    private String email;

    @Schema(description = "Data de nascimento do usuário", example = "1990-05-10")
    @NotNull(message = "Campo nascimento obrigatório")
    private LocalDate birthDate;

    @Schema(description = "Senha de acesso do usuário", example = "123456")
    @NotBlank(message = "Campo senha requerido")
    @Size(min = 6, max = 12, message = "Requerido de 6 a 12 caracteres.")
    private String password;

    //Atributos associados
    @Schema(description = "Dados do documento da pessoa (CPF ou CNPJ)")
    @NotNull
    @Valid
    private PersonDTO person;

    @Schema(description = "Lista de endereços associados ao usuário")
    @Valid
    private List<AddressDTO> addresses = new ArrayList<AddressDTO>();

    @Schema(description = "Lista de perfis de acesso atribuídos ao usuário", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private List<String> roles = new ArrayList<>();


    //Construtores
    public UserPersonAddressDTO() {
    }

    public UserPersonAddressDTO(Long id, String name, String email, LocalDate birthDate, String password, PersonDTO person, List<AddressDTO> addresses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
        this.person = person;
        this.addresses = addresses;
    }

    public UserPersonAddressDTO(User entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.birthDate = entity.getBirthDate();
        this.password = entity.getPassword();

        this.person = new PersonDTO(entity.getPerson());

        for (Address address : entity.getAddresses()) {
            this.addresses.add(new AddressDTO(address));
        }

        //Obter usuários locagos
        for (GrantedAuthority role : entity.getRoles()) {
            this.roles.add(role.getAuthority());
        }

    }


    //Métodos
    public void addAddresses(List<AddressDTO> addresses) {
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

    public PersonDTO getPerson() {
        return person;
    }

    public List<AddressDTO> getAddresses() {
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

    public void setPerson(@NotNull @Valid PersonDTO person) {
        this.person = person;
    }
}
