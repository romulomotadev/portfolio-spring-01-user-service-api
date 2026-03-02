package com.rpdevelopment.user_service_api.service;


import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import com.rpdevelopment.user_service_api.tests.UserFactory;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserPersonAddressServiceTest {

    @InjectMocks
    private UserPersonAddressService service;

    @Mock
    private UserRepository repository;

    private Long existingId;
    private Long nonExistingId;

    private User user;


    //INICIALIZAÇÃO
    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;

        //Criando Usuário
        user = UserFactory.createUser();

    }

    // ================= GET =================

    //ID EXISTENTE
    @Test
    public void findByIdShouldReturnUserWhenIdExists(){
        //Preparando
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(user));
        //Ação
        UserPersonAddressDto result = service.usersFindById(existingId);
        //Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(existingId);
        Assertions.assertThat(result.getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPerson().getDocument()).isEqualTo(user.getPerson().getDocument());
        Assertions.assertThat(result.getAddresses().get(1).getRoad()).isEqualTo(user.getAddresses().get(1).getRoad());
        //Verificação chamada de metodo
        Mockito.verify(repository).findById(existingId);
    }

    //ID NÃO EXISTENTE
    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        //Preparando
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        //Ação e Verificação
        Assertions.assertThatThrownBy(() -> service.usersFindById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
        //Verificação chamada metodo
        Mockito.verify(repository).findById(nonExistingId);
    }

    //FIND ALL PAGINADO
    @Test
    public void findAllPagedShouldReturnPageOfUserDto() {

        // Preparando
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        Mockito.when(repository.findAll(pageable)).thenReturn(userPage);

        // Ação
        Page<UserPersonAddressDto> result = service.usersFindAll(pageable);

        // Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(user.getId());

        Mockito.verify(repository).findAll(pageable);
    }
}
