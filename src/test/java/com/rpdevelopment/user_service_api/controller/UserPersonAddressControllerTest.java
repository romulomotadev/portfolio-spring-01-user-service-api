package com.rpdevelopment.user_service_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.exception.DuplicateResourceException;
import com.rpdevelopment.user_service_api.exception.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.service.UserPersonAddressService;
import com.rpdevelopment.user_service_api.tests.UserFactoryDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserPersonAddressController.class)
public class UserPersonAddressControllerTest {

    private Long existingId;
    private Long nonExistingId;
    private PageImpl<UserPersonAddressDto> pageDto;

    //DEPENDENTES
    @MockBean
    private UserPersonAddressService service;

    private UserPersonAddressDto dto;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dto = UserFactoryDto.createUserFactoryDto();
        pageDto = new PageImpl<>(List.of(dto));
    }

    // ================= GET =================

    //GET ID EXISTENTE
    @Test
    public void findByIdShouldReturnUserWhenIdExists() throws Exception {

        //CHAMADA E RETORNO DO METODO
        Mockito.when(service.usersFindById(existingId)).thenReturn(dto);

        //FIND ID - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/users/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));


        //VALIDAÇÃO - DADOS ESPERADOS JASON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("usuario@gmail.com"))
                .andExpect(jsonPath("$.person.document").value("507.332.198-64"))
                .andExpect(jsonPath("$.addresses[1].road").value("Rua A"));
    }

    //GET ID NÃO EXISTENTE
    @Test
    public void findByIdShouldReturn404WhenIdDoesNotExist() throws Exception {

        //CHAMADA E RETORNO DO METODO
        Mockito.when(service.usersFindById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("User not found"));

        //FIND ID - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.path").value("/users/" + nonExistingId));
    }

    // ================= CREATE =================
    @Test
    public void createShouldReturn201AndUserDto() throws Exception {

        //CHAMADA E RETORNO DO METODO
        Mockito.when(service.save(Mockito.any(UserPersonAddressDto.class)))
                .thenReturn(dto);

        //CORPO DA REQUISIÇÃO
        String jsonBody = objectMapper.writeValueAsString(dto);

        //POST
        ResultActions resultActions = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(header().exists("Location")) //Validação URI
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("usuario@gmail.com"))
                .andExpect(jsonPath("$.person.document").value("507.332.198-64"))
                .andExpect(jsonPath("$.addresses[1].road").value("Rua A"));
    }

    //CREATE EMAIL DUPLICADO
    @Test
    public void createShouldReturn409WhenEmailAlreadyExists() throws Exception {

        //CHAMADA E RETORNO DO METODO
        Mockito.when(service.save(Mockito.any(UserPersonAddressDto.class)))
                .thenThrow(new DuplicateResourceException("Email already exists"));

        //CORPO DA REQUISIÇÃO
        String jsonBody = objectMapper.writeValueAsString(dto);

        //POST
        ResultActions resultActions = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Email already exists"))
                .andExpect(jsonPath("$.path").value("/users"));

    }

    //VALIDAÇÃO
    @Test
    public void createShouldReturn422WhenValidationFails() throws Exception {

        // DTO INVALIDO (ex: nome vazio se tiver @NotBlank)
        UserPersonAddressDto invalidDto = new UserPersonAddressDto(
                null,
                "",          // inválido @NotBlank
                "email-invalido",  // inválido @Email
                null,              // inválido @NotNull
                "123",             // inválido @Size
                null,
                null
        );

        //CORPO DA REQUISIÇÃO
        String jsonBody = objectMapper.writeValueAsString(invalidDto);

        //POST
        ResultActions resultActions = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").exists());

        //VERIFICAÇÃO - service não chamado
        Mockito.verify(service, Mockito.never()).save(Mockito.any());
    }

    // ================= UPDATE =================
    //UPDATE ID EXISTENTE
    @Test
    public void updateShouldReturn200AndUpdatedUserWhenIdExists() throws Exception {

        //PREPARANDO
        Mockito.when(service.update(Mockito
                .any(UserPersonAddressDto.class), eq(existingId)))
                .thenReturn(dto);

        //CORPO DA REQUISIÇÃO - CONVERTE JAVA PARA JSON
        String jsonBody = objectMapper.writeValueAsString(dto);

        //UPDATE - CHAMADA
        // content(corpo da requisição) | contentType(formato corpo da requisição) | accept(formato da resposta)
        ResultActions resultActions =
                mockMvc.perform(put("/users/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("usuario@gmail.com"))
                .andExpect(jsonPath("$.person.document").value("507.332.198-64"))
                .andExpect(jsonPath("$.addresses[1].road").value("Rua A"));
    }

    //UPDATE ID NÃO EXISTENTE
    @Test
    public void updateShouldReturn404WhenIdDoesNotExist() throws Exception {

        //PREPARANDO
        Mockito.when(service.update(Mockito
                        .any(UserPersonAddressDto.class), eq(nonExistingId)))
                .thenThrow(new ResourceNotFoundException("User not found"));

        //CORPO DA REQUISIÇÃO - CONVERTE JAVA PARA JSON
        String jsonBody = objectMapper.writeValueAsString(dto);

        //UPDATE - CHAMADA
        // content(corpo da requisição) | contentType(formato corpo da requisição) | accept(formato da resposta)
        ResultActions resultActions =
                mockMvc.perform(put("/users/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO
        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("User not found"))
                .andExpect(jsonPath("$.path").value("/users/" + nonExistingId));

        //VERIFICA SERVICE CHAMOU O ID CORRETO E EXCEÇÃO VEIO DO SERVICE
        Mockito.verify(service)
                .update(Mockito.any(UserPersonAddressDto.class), eq(nonExistingId));
    }

    //UPDATE EMAIL DUPLICADO
    @Test
    public void updateShouldReturn409WhenEmailAlreadyExists() throws Exception{

        //CHAMADA E RETORNO DO METODO
        Mockito.when(service.update(Mockito
                .any(UserPersonAddressDto.class), eq(existingId)))
                .thenThrow(new DuplicateResourceException("Email already exists"));

        //CORPO DA REQUISIÇÃO - converter java para json
        String jsonBody = objectMapper.writeValueAsString(dto);

        //PUT
        ResultActions resultActions = mockMvc.perform(put("/users/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Email already exists"))
                .andExpect(jsonPath("$.path").value("/users/" + existingId));

        //VERIFICA SE CHAMOU O SERVICE, E VEIO DA CAMADA DE NEGOCIO
        Mockito.verify(service)
                .update(Mockito.any(UserPersonAddressDto.class), eq(existingId));
    }

    //UPDATE VALIDAÇÃO
    @Test
    public void updateShouldReturn422WhenValidationFails() throws Exception {

        // DTO INVALIDO (ex: nome vazio se tiver @NotBlank)
        UserPersonAddressDto invalidDto = new UserPersonAddressDto(
                existingId,
                "",          // inválido @NotBlank
                "email-invalido",  // inválido @Email
                null,              // inválido @NotNull
                "123",             // inválido @Size
                null,
                null
        );

        //CORPO DA REQUISIÇÃO - converter java para json
        String jsonBody = objectMapper.writeValueAsString(invalidDto);

        //PUT
        ResultActions resultActions = mockMvc.perform(put("/users/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.errors").isArray());;

        Mockito.verify(service, Mockito.never())
                .update(Mockito.any(UserPersonAddressDto.class), Mockito.any());
    }

    // ================= DELETE =================
    //DELETE ID EXISTENTE
    @Test
    public void deleteShouldReturn204WhenIdExists() throws Exception {
        //PREPARANDO
        Mockito.doNothing().when(service).delete(existingId);

        //AÇÃO
        ResultActions resultActions =
                mockMvc.perform(delete("/users/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        //ASSERTIONS - VALIDAÇÃO
        resultActions.andExpect(status().isNoContent());

        Mockito.verify(service).delete(existingId);
    }

    //DELETE ID NÃO EXISTENTE
    @Test
    public void deleteShouldReturn404WhenIdDoesNotExist() throws Exception {

        //PREPARANDO
        Mockito.doThrow(new ResourceNotFoundException("User not found"))
                .when(service).delete(nonExistingId);

        //AÇÃO
        ResultActions resultActions =
                mockMvc.perform(delete("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").exists());

        Mockito.verify(service).delete(nonExistingId);
    }

    // ================= PAGINAÇÃO =================
    //PAGINAÇÃO
    @Test
    public void findAllShouldReturnPagedUsers() throws Exception {

        //PREPARANDO
        Mockito.when(service.usersFindAll(Mockito.any()))
                .thenReturn(pageDto);

        //AÇÃO
        ResultActions resultActions =
                mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(1));

        //VERIFICAÇÃO CHAMADA NO SERVICE
        Mockito.verify(service).usersFindAll(Mockito.any());
    }
}
