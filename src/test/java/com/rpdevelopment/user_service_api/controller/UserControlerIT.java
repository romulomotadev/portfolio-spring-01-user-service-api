package com.rpdevelopment.user_service_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.service.UserPersonAddressService;
import com.rpdevelopment.user_service_api.tests.TokenUtil;
import com.rpdevelopment.user_service_api.tests.UserFactoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
    private TokenUtil tokenUtil;


    //================== ATRIBUTOS ==================

    private Long existingId;
    private Long nonExistingId;
    private UserPersonAddressDto dto;
    private String bearerToken;


    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dto = UserFactoryDto.createNewUserFactoryDto();

        //Inicialização token user admin
        String username = "admin@gmail.com";
        String password = "123456";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
    }


    //================== GET ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Busca por ID retornar usuário quando ID existe")
    public void findByIdShouldReturnUserWhenIdExists() throws Exception {

        //Preparando
        UserPersonAddressDto savedDto = service.save(dto);
        Long idParaBuscar = savedDto.getId();

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(get("/users/{id}", idParaBuscar)
                        .header("Authorization", "Bearer " + bearerToken)
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
    @DisplayName("Busca por ID retornar 404 quando ID não existe")
    public void findByIdShouldReturn404WhenIdDoesNotExist() throws Exception {

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(get("/users/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.path").value("/users/" + nonExistingId));
    }


    //================== CREATE ==================

    // NOVO CLIENTE
    @Test
    @DisplayName("Create deve retornar 201 quando dados forem Válidos")
    public void createShouldReturn201WhenDataIsValid() throws Exception {

        //Corpo da requisição
        String requestBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc
                .perform(post("/users")
                        .header("Authorization", "Bearer " + bearerToken)
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
    @DisplayName("Create deve retornar 409 quando E-mail existe")
    public void createShouldReturn409WhenEmailAlreadyExists() throws Exception {

        //Corpo da requisição
        dto.setEmail("email@gmail.com");
        service.save(dto);
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer " + bearerToken)
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
    @DisplayName("Create deve retornar 409 quando Documento existe")
    public void createShouldReturn409WhenDocumentExists() throws Exception {

        //Corpo da requisição
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");
        service.save(dto);
        dto.setEmail("email@gmail.com");
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer " + bearerToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Document already exists"))
                .andExpect(jsonPath("$.path").value("/users"));
    }


    //================== UPDATE ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Update deve retornar 200 quando ID existe")
    public void updateShouldReturn200WhenIdExists() throws Exception {

        //Preparando
        dto.setName("Usuario Atualizado");
        service.update(dto, existingId);

        //Corpo da requisição
        String requestBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(put("/users/{id}", existingId)
                        .header("Authorization", "Bearer " + bearerToken)
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
    @DisplayName("Update deve retornar 404 quando ID não existe")
    public void updateShouldReturn404WhenIdDoesNotExist() throws Exception {

        //Corpo da requisição
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(put("/users/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + bearerToken)
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
    @DisplayName("Update deve retornar 409 quando E-mail existe")
    public void updateShouldReturn409WhenEmailExists() throws Exception {

        //Preparando
        dto.setEmail("email@gmail.com");
        service.save(dto);
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");

        //Corpo da requisição
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc.perform(put("/users/{id}", existingId)
                .header("Authorization", "Bearer " + bearerToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Email already exists"))
                .andExpect(jsonPath("$.path").value("/users/" + existingId));
    }


    //DOCUMENTO DUPLICADO
    @Test
    @DisplayName("Update deve retornar 409 quando Documento existe")
    public void updateShouldReturn409WhenDocumentExists() throws Exception {

        //Preparando
        dto.getPerson().setDocument("xxx.xxx.xxx-xx");
        service.save(dto);
        dto.setEmail("email@gmail.com");

        //Corpo da requisição
        String jsonBody = objectMapper.writeValueAsString(dto);

        //Chamada / Ação
        ResultActions resultActions = mockMvc.perform(put("/users/{id}", existingId)
                .header("Authorization", "Bearer " + bearerToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Document already exists"))
                .andExpect(jsonPath("$.path").value("/users/" + existingId));
    }


    //================== DELETE ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Delete deve retornar 204 quando ID existe")
    public void deleteShouldReturn204WhenIdExists() throws Exception {

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(delete("/users/{id}", existingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isNoContent());
    }


    //ID NÃO EXISTENTE
    @Test
    @DisplayName("Delete deve retornar 404 quando ID não existe")
    public void deleteShouldReturn404WhenIdDoesNotExist() throws Exception {

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(delete("/users/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + bearerToken)
                        .accept(MediaType.APPLICATION_JSON));

        //Validações
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").exists());
    }


    // ================= PAGINAÇÃO =================

    //GET PAGINADO
    @Test
    @DisplayName("Todos os usuários paginados devem retornar")
    public void findAllShouldReturnPagedUsers() throws Exception {

        //Chamada / Ação
        ResultActions resultActions =
                mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + bearerToken)
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


    //================== SECURITY ==================

    @Test
    @DisplayName("Busca deve retornar 200 quando Admin acessa lista de usuários")
    public void findAllShouldReturnOkWhenAdminIsLogged() throws Exception {

        ////Chamada / Ação / Verificação
        ResultActions resultActions =
            mockMvc.perform(get("/users")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))) // Simula o JWT
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Busca deve retornar 403 quando Usuário comum tenta acessar lista de usuários")
    public void findAllShouldReturnForbiddenWhenUserIsLogged() throws Exception {

        mockMvc.perform(get("/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Busca deve retornar 401 quando NÃO HÁ usuário logado")
    public void findAllShouldReturnUnauthorizedWhenNoUser() throws Exception {

        ////Chamada / Ação / Verificação
        ResultActions resultActions =
            mockMvc.perform(get("/users")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Busca deve retornar 200 quando Usuário comum acessa o SEU PRÓPRIO ID")
    public void findByIdShouldReturnOkWhenUserAccessSelfId() throws Exception {
        Long selfId = 1L;
        String selfEmail = "admin@gmail.com";

        ////Chamada / Ação / Verificação
        ResultActions resultActions =
            mockMvc.perform(get("/users/{id}", selfId)
                            .with(jwt()
                                    .jwt(builder -> builder.claim("username", selfEmail)) // Define o email no token
                                    .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }


    @Test
    @DisplayName("Busca eve retornar 403 quando usuário comum tenta acessar ID de OUTRO usuário")
    public void findByIdShouldReturnForbiddenWhenUserAccessOtherId() throws Exception {
        Long otherId = 2L;
        String myEmail = "diego.m@gmail.com"; // Eu sou o Diego (ID 10), tentando ver o ID 2

        ////Chamada / Ação / Verificação
        ResultActions resultActions =
            mockMvc.perform(get("/users/{id}", otherId)
                            .with(jwt()
                                    .jwt(builder -> builder.claim("username", myEmail))
                                    .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
    }
}
