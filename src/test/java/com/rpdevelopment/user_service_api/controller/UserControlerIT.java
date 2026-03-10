package com.rpdevelopment.user_service_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import com.rpdevelopment.user_service_api.service.UserPersonAddressService;
import com.rpdevelopment.user_service_api.tests.UserFactoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControlerIT {

    //================== DEPENDÊNCIAS ==================

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserPersonAddressService service;
    @Autowired
    private UserRepository repository;

    //================== ATRIBUTOS ==================

    private Long existingId;
    private Long nonExistingId;
    private UserPersonAddressDto dto;

    //============ INICIALIZAR ATRIBUTOS ============

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        dto = UserFactoryDto.createNewUserFactoryDto();
    }

    //================== CREATE ==================

    // NOVO CLIENTE
    @Test
    public void createShouldReturn201WhenDataIsValid() throws Exception {

        //Corpo da requisição
        String requestBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc
                .perform(post("/users")
                .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber()) // ID EXISTE é um número?
                .andExpect(header().exists("Location")) //Validação URI
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("usuario@gmail.com"))
                .andExpect(jsonPath("$.person.document").value("507.332.198-64"))
                .andExpect(jsonPath("$.addresses[0].road").value("Rua dos Bobos"));
    }

    // EMAIL DUPLICADO
    @Test
    public void createShouldReturn409WhenEmailAlreadyExists() throws Exception {

        //Corpo da requisição
        dto.setEmail("email@gmail.com");
        service.save(dto);
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Email already exists"))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    // DOCUMENTO DUPLICADO
    @Test
    public void createShouldReturn409WhenDocumentExists() throws Exception {

        //Corpo da requisição
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");
        service.save(dto);
        dto.setEmail("email@gmail.com");
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Document already exists"))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    //================== GET ==================

    //ID EXISTENTE
    @Test
    public void findByIdShouldReturnUserWhenIdExists() throws Exception {

        //Preparando
        UserPersonAddressDto savedDto = service.save(dto);
        Long idParaBuscar = savedDto.getId();

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(get("/users/{id}", idParaBuscar)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber()) // ID EXISTE é um número?
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("usuario@gmail.com"))
                .andExpect(jsonPath("$.person.document").value("507.332.198-64"))
                .andExpect(jsonPath("$.addresses[0].road").value("Rua dos Bobos"));
    }

    //ID NÃO EXISTENTE
    @Test
    public void findByIdShouldReturn404WhenIdDoesNotExist() throws Exception {

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(get("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.path").value("/users/" + nonExistingId));
    }

    //GET PAGINADO
    @Test
    public void findAllShouldReturnPagedUsers() throws Exception {

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(1));
    }

    //================== UPDATE ==================

    //ID EXISTENTE
    @Test
    public void updateShouldReturn200WhenIdExists() throws Exception {

        //Preparando
        dto.setName("Usuario Atualizado");
        service.update(dto, existingId);

        //Corpo da requisição
        String requestBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(put("/users/{id}", existingId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Usuario Atualizado"))
                .andExpect(jsonPath("$.email").value("usuario@gmail.com"))
                .andExpect(jsonPath("$.person.document").value("507.332.198-64"))
                .andExpect(jsonPath("$.addresses[0].road").value("Rua dos Bobos"));
    }

    //ID NÃO EXISTENTE
    @Test
    public void updateShouldReturn404WhenIdDoesNotExist() throws Exception {

        //Corpo da requisição
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(put("/users/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Id Not Found"))
                .andExpect(jsonPath("$.path").value("/users/" + nonExistingId));
    }

    //EMAIL DUPLICADO
    @Test
    public void updateShouldReturn409WhenEmailExists() throws Exception {

        //Preparando
        dto.setEmail("email@gmail.com");
        service.save(dto);
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");

        //Corpo da requisição
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
    }

    //DOCUMENTO DUPLICADO
    @Test
    public void updateShouldReturn409WhenDocumentExists() throws Exception {

        //Preparando
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");
        service.save(dto);
        dto.setEmail("email@gmail.com");

        //Corpo da requisição
        String jsonBody = objectMapper.writeValueAsString(dto);

        //PUT
        ResultActions resultActions = mockMvc.perform(put("/users/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //RESPOSTA ESPERADA
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Document already exists"))
                .andExpect(jsonPath("$.path").value("/users/" + existingId));
    }

}
