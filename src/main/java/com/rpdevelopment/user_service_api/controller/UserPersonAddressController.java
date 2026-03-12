package com.rpdevelopment.user_service_api.controller;

import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.projection.UserAddressProjection;
import com.rpdevelopment.user_service_api.projection.UserDocumentProjection;
import com.rpdevelopment.user_service_api.service.UserPersonAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserPersonAddressController {

    @Autowired
    private UserPersonAddressService userPersonAddressService;

    //CRUD PADRÃO
    //FIND ALL
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserPersonAddressDto>> userFindAll(Pageable pageable) {
        Page<UserPersonAddressDto> usersDto = userPersonAddressService.usersFindAll(pageable);
        return ResponseEntity.ok(usersDto);
    }

    //FIND BY ID
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> userFindById(@PathVariable Long id) {
        UserPersonAddressDto userDto = userPersonAddressService.usersFindById(id);
        return ResponseEntity.ok(userDto);
    }

    //QUERY USER DOCUMENT
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/documents")
    public ResponseEntity<Page<UserDocumentProjection>> searchUserDocument(Pageable pageable) {
        Page<UserDocumentProjection> usersDocummentProjection = userPersonAddressService.searchUserDocument(pageable);
        return ResponseEntity.ok(usersDocummentProjection);
    }


    //QUERY USER ADDRESS
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/addresses")
    public ResponseEntity<Page<UserAddressProjection>> searchUserAddress(Pageable pageable) {
        Page<UserAddressProjection> userAddressProjections = userPersonAddressService.searchUserAddress(pageable);
        return ResponseEntity.ok(userAddressProjections);
    }

    //POST
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserPersonAddressDto> save(@RequestBody @Valid UserPersonAddressDto userDto) {
        UserPersonAddressDto userDtoSaved = userPersonAddressService.save(userDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDtoSaved.getId()).toUri();
        return ResponseEntity.created(uri).body(userDtoSaved);
    }

    //PUT
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> update(@RequestBody @Valid UserPersonAddressDto userDto, @PathVariable Long id) {
        UserPersonAddressDto userDtoUpdated = userPersonAddressService.update(userDto, id);
        return ResponseEntity.ok(userDtoUpdated);
    }

    //DELETE
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserPersonAddressDto> delete(@PathVariable Long id) {
        userPersonAddressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
