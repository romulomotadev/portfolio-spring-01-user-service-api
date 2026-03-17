package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.AddressDto;
import com.rpdevelopment.user_service_api.dto.PersonDto;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.projection.UserAddressProjection;
import com.rpdevelopment.user_service_api.projection.UserDocumentProjection;
import com.rpdevelopment.user_service_api.service.UserService;
import com.rpdevelopment.user_service_api.service.UserPersonAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jdk.jfr.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Users", description = "Controller for Users")
@RequestMapping(value = "/users")
public class UserPersonAddressController {

    // =============== DEPENDÊNCIAS ====================

    private UserPersonAddressService service;


    // ========= CONSTRUTOR DEPENDÊNCIAS ===============

    public UserPersonAddressController(UserPersonAddressService service) {
        this.service = service;
    }


    // =============== GET ====================

    //FIND ALL
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Buscar todos usuários.",
            description = "Retorna todos os usuários de forma paginada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
            })
    @GetMapping
    public ResponseEntity<Page<UserPersonAddressDto>> userFindAll(Pageable pageable) {
        Page<UserPersonAddressDto> usersDto = service.usersFindAll(pageable);
        return ResponseEntity.ok(usersDto);
    }


    //FIND BY ID
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna um usuário com base no ID informado. Caso o usuário não seja encontrado, será retornado status 404.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> userFindById(@PathVariable Long id) {
        UserPersonAddressDto userDto = service.usersFindById(id);
        return ResponseEntity.ok(userDto);
    }


    //QUERY USER DOCUMENT
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Buscar todos os usuários com documentos",
            description = "Retorna uma lista de usuários contendo email e documento (CPF ou CNPJ).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido")
            })
    @GetMapping(value = "/documents")
    public ResponseEntity<Page<UserDocumentProjection>> searchUserDocument(Pageable pageable) {
        Page<UserDocumentProjection> usersDocummentProjection = service.searchUserDocument(pageable);
        return ResponseEntity.ok(usersDocummentProjection);
    }


    //QUERY USER ADDRESS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Buscar todos os usuários com endereços",
            description = "Retorna uma lista de usuários contendo seus endereços.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido")
            })
    @GetMapping(value = "/addresses")
    public ResponseEntity<Page<UserAddressProjection>> searchUserAddress(Pageable pageable) {
        Page<UserAddressProjection> userAddressProjections = service.searchUserAddress(pageable);
        return ResponseEntity.ok(userAddressProjections);
    }


    // =============== POST ====================

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Criar novo usuário",
            description = "Cria um novo usuário e retorna seus dados.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Novo usuário criado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "409", description = "Conflito de dados: dados já existentes no banco de dados"),
                    @ApiResponse(responseCode = "422", description = "Dados inválidos"),
            })
    @PostMapping
    public ResponseEntity<UserPersonAddressDto> save(@RequestBody @Valid UserPersonAddressDto userDto) {
        UserPersonAddressDto userDtoSaved = service.save(userDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDtoSaved.getId()).toUri();
        return ResponseEntity.created(uri).body(userDtoSaved);
    }


    // =============== UPDATE ====================

    //UPDATE ALL
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Atualizar usuário por ID",
            description = "Atualiza um usuário existente no banco de dados e retorna seus dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "409", description = "Conflito de dados: dados já existentes no banco de dados"),
                    @ApiResponse(responseCode = "422", description = "Dados inválidos"),
            })
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> update(@RequestBody @Valid UserPersonAddressDto userDto,
                                                       @PathVariable Long id) {

        UserPersonAddressDto userDtoUpdated = service.update(userDto, id);
        return ResponseEntity.ok(userDtoUpdated);
    }


    //UPDATE PERSON
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Atualizar documento de usuário",
            description = "Atualiza o documento de um usuário existente no banco de dados e retorna os dados atualizados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "409", description = "Conflito de dados: dados já existentes no banco de dados"),
                    @ApiResponse(responseCode = "422", description = "Dados inválidos"),
            })
    @PutMapping(value = "/{id}/person")
    public ResponseEntity<PersonDto> updatePerson(@RequestBody @Valid PersonDto personDto,
                                                  @PathVariable Long id) {

        PersonDto personDtoUpdate = service.updatePerson(personDto, id);
        return ResponseEntity.ok(personDtoUpdate);
    }


    //UPDATE ADDRESSES
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Atualizar endereços de usuário",
            description = "Atualiza o endereços de um usuário existente no banco de dados e retorna os dados atualizados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "422", description = "Dados inválidos"),
            })
    @PutMapping("/{id}/addresses")
    public ResponseEntity<List<AddressDto>> updateAddresses(
            @RequestBody @Valid List<AddressDto> addressDto,
            @PathVariable Long id) {

        List<AddressDto> addressesDtoUpdate = service.updateAddresses(addressDto, id);

        return ResponseEntity.ok(addressesDtoUpdate);
    }


    // =============== DELETE ====================

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Excluir usuário por ID",
            description = "Exclui um usuário com base no ID informado. Caso o usuário não seja encontrado, será retornado status 404.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso proibido"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
