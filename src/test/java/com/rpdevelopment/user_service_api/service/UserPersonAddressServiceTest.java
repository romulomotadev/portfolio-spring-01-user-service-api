package com.rpdevelopment.user_service_api.service;

import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.DuplicateResourceException;
import com.rpdevelopment.user_service_api.exception.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.repository.AddressRepository;
import com.rpdevelopment.user_service_api.repository.PersonRepository;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import com.rpdevelopment.user_service_api.tests.UserFactory;
import com.rpdevelopment.user_service_api.tests.UserFactoryDto;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class UserPersonAddressServiceTest {

    @InjectMocks
    private UserPersonAddressService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AuthService authService;

    private Long existingId;
    private Long nonExistingId;

    private User user;
    private UserPersonAddressDto userDto;


    //INICIALIZAÇÃO
    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;

        //Criando Usuário
        user = UserFactory.createValidUserWithId();
    }

    // ================= GET =================

    //ID EXISTENTE
    @Test
    public void findByIdShouldReturnUserWhenIdExists(){
        //Preparando
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        //Ação
        UserPersonAddressDto result = service.usersFindById(existingId);

        //Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(existingId);
        Assertions.assertThat(result.getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPerson().getDocument()).isEqualTo(user.getPerson().getDocument());
        Assertions.assertThat(result.getAddresses().get(0).getRoad()).isEqualTo(user.getAddresses().get(0).getRoad());

        //Verificação chamada
        Mockito.verify(userRepository).findById(existingId);
    }

    //ID NÃO EXISTENTE
    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        //Preparando
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Ação e Verificação
        Assertions.assertThatThrownBy(() -> service.usersFindById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);

        //Verificação chamada metodo
        Mockito.verify(userRepository).findById(nonExistingId);
    }

    //FIND ALL PAGINADO
    @Test
    public void findAllPagedShouldReturnPageOfUserDto() {

        // Preparando
        // Criando objeto de paginação (pagina 0 tamanho 10)
        Pageable pageable = PageRequest.of(0, 10);
        // Simulando retorno
        // - List.of(user) → conteúdo da página
        // - pageable → informações da página (número, tamanho)
        // - total de elementos no banco
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        Mockito.when(userRepository.findAll(pageable)).thenReturn(userPage);

        // Ação
        Page<UserPersonAddressDto> result = service.usersFindAll(pageable);

        // Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1); // O total de elementos da página é 1
        Assertions.assertThat(result.getContent()).hasSize(1); // Confirma que a lista dentro da página tem 1 item.
        Assertions.assertThat(result.getContent().get(0).getId()).isEqualTo(user.getId());

        //Verificação Chamada
        Mockito.verify(userRepository).findAll(pageable);
    }

    // ================= CREATE =================

    //DADOS VALIDOS
    @Test
    public void createShouldReturnUserDto(){

        //Preparando
        UserPersonAddressDto userDto = UserFactoryDto.createValidUserFactoryDto();
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        //Ação
        UserPersonAddressDto result = service.save(userDto);

        //Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(existingId);
        Assertions.assertThat(result.getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPerson().getDocument()).isEqualTo(user.getPerson().getDocument());
        Assertions.assertThat(result.getAddresses().get(1).getRoad()).isEqualTo(user.getAddresses().get(1).getRoad());

        //Verificação chamada
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    //EMAIL DUPLICADO
    @Test
    public void saveShouldThrowDuplicateResourceExceptionWhenEmailAlreadyExists(){

        //Preparando
        UserPersonAddressDto duplicateEmailDto = UserFactoryDto.createValidUserFactoryDto();
        duplicateEmailDto.setEmail("duplicateEmail@gmail.com");

        Mockito.when(userRepository.existsByEmail("duplicateEmail@gmail.com")).thenReturn(true);

        //Ação + Verificação
        Assertions.assertThatThrownBy(() -> service.save(duplicateEmailDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");
    }

    //DOCUMENTO DUPLICADO
    @Test
    public void saveShouldThrowDuplicateResourceExceptionWhenDocumentAlreadyExists(){

        //Preparando
        UserPersonAddressDto duplicateDocumentDto = UserFactoryDto.createValidUserFactoryDto();
        duplicateDocumentDto.getPerson().setDocument("duplicateDocument");

        Mockito.when(personRepository.existsByDocument("duplicateDocument")).thenReturn(true);

        //Ação + Verificação
        Assertions.assertThatThrownBy(() -> service.save(duplicateDocumentDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Document already exists");;

    }

    // ================= UPDATE =================

    //ID EXISTENTE
    @Test
    public void updateByIdExistentShouldUpdateEntity() {

        //Preparando
        UserPersonAddressDto updateDto = UserFactoryDto.createValidUserFactoryDto();

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        Mockito.when(addressRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(user.getAddresses().get(0));

        //Ação
        UserPersonAddressDto result = service.update(updateDto, existingId);

        //Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(existingId);
        Assertions.assertThat(result.getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPerson().getDocument()).isEqualTo(user.getPerson().getDocument());
        Assertions.assertThat(result.getAddresses().get(0).getRoad()).isEqualTo(user.getAddresses().get(0).getRoad());
    }

    //ID NÃO EXISTENTE
    @Test
    public void updateByIdNonExistenteThrowEntityNotFoundExceptionWhenIdDoesNotExist(){

        //Preparando
        UserPersonAddressDto updateDto = UserFactoryDto.createValidUserFactoryDto();
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Ação || Validação
        Assertions.assertThatThrownBy(() -> service.update(updateDto, nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    //EMAIL DUPLICADO
    @Test
    public void updateShouldThrowDuplicateResourceExceptionWhenEmailAlreadyExists(){

        //Preparando
        UserPersonAddressDto duplicateEmailDto = UserFactoryDto.createValidUserFactoryDto();
        duplicateEmailDto.setEmail("duplicateEmail@gmail.com");

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        Mockito.when(userRepository.existsByEmailAndIdNot("duplicateEmail@gmail.com", existingId))
                .thenReturn(true);;

        //Ação + Verificação
        Assertions.assertThatThrownBy(() -> service.update(duplicateEmailDto, existingId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");
    }

    //DOCUMENTO DUPLICADO
    @Test
    public void updateShouldThrowDuplicateResourceExceptionWhenDocumentAlreadyExists(){

        //Preparando
        UserPersonAddressDto duplicateDocumentDto = UserFactoryDto.createValidUserFactoryDto();
        duplicateDocumentDto.getPerson().setDocument("duplicateDocument");

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        Mockito.when(personRepository.existsByDocumentAndIdNot("duplicateDocument", existingId))
                .thenReturn(true);;

        //Ação + Verificação
        Assertions.assertThatThrownBy(() -> service.update(duplicateDocumentDto, existingId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Document already exists");
    }

    // ================= DELETE =================

    //ID EXISTENTE
    @Test
    public void deleteShouldWhenIdExists(){

        //Preparando
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        //Ação + Verificação
        Assertions.assertThatCode(() -> service.delete(existingId))
                .doesNotThrowAnyException();

        //verificação chamada
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    //ID INEXISTENTE
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        //Preparando
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Ação Verificação
        Assertions.assertThatThrownBy(()-> service.delete(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Id Not Found");;

        // Garantir que o delete NUNCA foi chamado
        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any());
    }

    //ID DEPENDENTE
    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId(){

        //Preparando
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        Mockito.doThrow(DataIntegrityViolationException.class).when(userRepository).delete(user);

        //Ação Verificação
        Assertions.assertThatThrownBy(()-> service.delete(existingId))
                .isInstanceOf(DataIntegrityViolationException.class);

        // Garantir que o delete NUNCA foi chamado
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

}
