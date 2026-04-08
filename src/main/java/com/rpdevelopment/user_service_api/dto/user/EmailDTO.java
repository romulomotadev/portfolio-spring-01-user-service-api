package com.rpdevelopment.user_service_api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {

	//======= ATRIBUTOS ========

    @NotBlank(message = "Campo Obrigatório")
    @Email(message = "Email invalido")
	private String email;

	//======= CONSTRUTORES ========

	public EmailDTO() {
	}

	public EmailDTO(String email) {
		this.email = email;
	}

	//======= GETTER ========

	public String getEmail() {
		return email;
	}
}
