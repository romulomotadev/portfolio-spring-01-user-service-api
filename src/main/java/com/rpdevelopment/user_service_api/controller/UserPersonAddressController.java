package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.AddressDto;
import com.rpdevelopment.user_service_api.dto.PersonDto;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.projection.UserAddressProjection;
import com.rpdevelopment.user_service_api.projection.UserDocumentProjection;
import com.rpdevelopment.user_service_api.service.UserService;
import com.rpdevelopment.user_service_api.service.UserPersonAddressService;
import jakarta.validation.Valid;
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
    @GetMapping
    public ResponseEntity<Page<UserPersonAddressDto>> userFindAll(Pageable pageable) {
        Page<UserPersonAddressDto> usersDto = service.usersFindAll(pageable);
        return ResponseEntity.ok(usersDto);
    }


    //FIND BY ID
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> userFindById(@PathVariable Long id) {
        UserPersonAddressDto userDto = service.usersFindById(id);
        return ResponseEntity.ok(userDto);
    }


    //QUERY USER DOCUMENT
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/documents")
    public ResponseEntity<Page<UserDocumentProjection>> searchUserDocument(Pageable pageable) {
        Page<UserDocumentProjection> usersDocummentProjection = service.searchUserDocument(pageable);
        return ResponseEntity.ok(usersDocummentProjection);
    }


    //QUERY USER ADDRESS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/addresses")
    public ResponseEntity<Page<UserAddressProjection>> searchUserAddress(Pageable pageable) {
        Page<UserAddressProjection> userAddressProjections = service.searchUserAddress(pageable);
        return ResponseEntity.ok(userAddressProjections);
    }


    // =============== POST ====================

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserPersonAddressDto> save(@RequestBody @Valid UserPersonAddressDto userDto) {
        UserPersonAddressDto userDtoSaved = service.save(userDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDtoSaved.getId()).toUri();
        return ResponseEntity.created(uri).body(userDtoSaved);
    }


    // =============== UPDATE ====================

    //UPDATE ALL
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> update(@RequestBody @Valid UserPersonAddressDto userDto,
                                                       @PathVariable Long id) {

        UserPersonAddressDto userDtoUpdated = service.update(userDto, id);
        return ResponseEntity.ok(userDtoUpdated);
    }


    //UPDATE PERSON
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/person")
    public ResponseEntity<PersonDto> updatePerson(@RequestBody @Valid PersonDto personDto,
                                                  @PathVariable Long id) {

        PersonDto personDtoUpdate = service.updatePerson(personDto, id);
        return ResponseEntity.ok(personDtoUpdate);
    }


    //UPDATE ADDRESSES
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}/addresses")
    public ResponseEntity<List<AddressDto>> updateAddresses(
            @RequestBody @Valid List<AddressDto> addressDto,
            @PathVariable Long id) {

        List<AddressDto> addressesDtoUpdate = service.updateAddresses(addressDto, id);

        return ResponseEntity.ok(addressesDtoUpdate);
    }


    // =============== DELETE ====================

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
