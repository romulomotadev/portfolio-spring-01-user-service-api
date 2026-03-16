package com.rpdevelopment.user_service_api.service;

import com.rpdevelopment.user_service_api.dto.AddressDto;
import com.rpdevelopment.user_service_api.dto.PersonDto;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.Person;
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
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserPersonAddressServiceTest {

    //================== DEPENDÊNCIAS ==================

    @Mock
    private UserService authService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private UserPersonAddressService service;

    private User user;
    private UserPersonAddressDto userDto;


    //================== ATRIBUTOS ==================

    private Long existingId;
    private Long nonExistingId;


    //============ INICIALIZAÇÃO ============

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
    @DisplayName("Busca deve retornar usuário quando ID existir")
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
        Assertions.assertThat(result.getAddresses().getFirst().getRoad()).isEqualTo(user.getAddresses().getFirst().getRoad());

        //Verificação chamada
        Mockito.verify(userRepository).findById(existingId);
    }


    //ID NÃO EXISTENTE
    @Test
    @DisplayName("Busca por ID deve retornar Not Found Exception quando ID não existe")
    public void SearchByIdShouldNotFoundExceptionWhenIdNotFound(){

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
    @DisplayName("Buscar todas as paginas deve retornar a pagina do usuário DTO")
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
        Assertions.assertThat(result.getContent().getFirst().getId()).isEqualTo(user.getId());

        //Verificação Chamada
        Mockito.verify(userRepository).findAll(pageable);
    }


    // ================= CREATE =================

    //DADOS VALIDOS
    @Test
    @DisplayName("Create deve retornar User DTO")
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
    @DisplayName("Create deve retornar Duplicate Exception quando E-mail existe")
    public void createShouldReturnDuplicateExceptionWhenEmailAlreadyExists(){

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
    @DisplayName("Create deve retornar Exception quando Document existe")
    public void createShouldReturnExceptionWhenDocumentAlreadyExists(){

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
    @DisplayName("Update por ID deve atualizar entidade")
    public void updateByIdShouldUpdateEntity() {

        //Preparando
        UserPersonAddressDto updateDto = UserFactoryDto.createValidUserFactoryDto();

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        Mockito.when(addressRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(user.getAddresses().getFirst());

        //Ação
        UserPersonAddressDto result = service.update(updateDto, existingId);

        //Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(existingId);
        Assertions.assertThat(result.getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPerson().getDocument()).isEqualTo(user.getPerson().getDocument());
        Assertions.assertThat(result.getAddresses().getFirst().getRoad()).isEqualTo(user.getAddresses()
                .getFirst().getRoad());
    }


    //UPDATE PERSON ID EXISTENTE
    @Test
    @DisplayName("Update deve atualizar person quando id do usuário existe")
    public void updatePersonShouldUpdateWhenUserExists() {

        // Preparando
        PersonDto personDto = UserFactoryDto.createValidUserFactoryDto().getPerson();
        personDto.setId(existingId);

        Mockito.when(userRepository.findById(existingId))
                .thenReturn(Optional.of(user));

        // Ação
        PersonDto result = service.updatePerson(personDto, existingId);

        // Verificação
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getDocument()).isEqualTo(personDto.getDocument());
        Assertions.assertThat(result.getType()).isEqualTo(personDto.getType());
    }


    //UPDATE ADDRESSES ID EXISTENTE
    @Test
    @DisplayName("Deve atualizar Addresses quando id do usuário existe")
    public void updateAddressesShouldUpdateWhenUserExists() {
        // Preparando dados
        List<AddressDto> addressDtos = UserFactoryDto.createValidUserFactoryDto().getAddresses();

        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        // Se o DTO tem ID, moca o addressRepository
        if (addressDtos.getFirst().getId() != null) {
            Mockito.when(addressRepository.getReferenceById(ArgumentMatchers.anyLong()))
                    .thenReturn(user.getAddresses().getFirst());
        }

        // Ação
        List<AddressDto> result = service.updateAddresses(addressDtos, existingId);

        // Verificação
        Assertions.assertThat(result).isNotEmpty();
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }


    //ID NÃO EXISTENTE
    @Test
    @DisplayName("Update deve retornar Not Found Exception quando Document não existe")
    public void updateShouldReturnNotFoundExceptionWhenDocumentNotExist(){

        //Preparando
        UserPersonAddressDto updateDto = UserFactoryDto.createValidUserFactoryDto();
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Ação || Validação
        Assertions.assertThatThrownBy(() -> service.update(updateDto, nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);

    }


    //ID NÃO EXISTENTE - USER PERSON
    @Test
    @DisplayName("Update Person deve lançar exceção quando usuário não existe")
    public void updatePersonShouldThrowExceptionWhenUserNotFound(){

        //Preparando
        PersonDto persnDto = UserFactoryDto.createValidUserFactoryDto().getPerson();
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Ação || Validação
        Assertions.assertThatThrownBy(() -> service.updatePerson(persnDto, nonExistingId));
    }


    //ID NÃO EXISTENTE - USER ADDRESSES
    @Test
    @DisplayName("Update Addresses deve lançar exceção quando usuário não existe")
    public void updateAddressesShouldThrowExceptionWhenUserNotFound(){
        // Preparando
        List<AddressDto> addressDtos = UserFactoryDto.createValidUserFactoryDto().getAddresses();

        // CORREÇÃO: Use userRepository, que é o que o service chama primeiro
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Ação || Validação
        Assertions.assertThatThrownBy(() -> service.updateAddresses(addressDtos, nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }


    //EMAIL DUPLICADO
    @Test
    @DisplayName("Update deve retornar Duplicate Exception quando E-mail existe")
    public void updateShouldReturnExceptionDuplicateWhenEmailExists(){

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
    @DisplayName("Update deve retornar Duplicate Exception quando Document existe")
    public void updateShouldReturnExceptionDuplicateWhenDocumentExists(){

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
    @DisplayName("Delete deve excluir quando ID existe")
    public void deleteShouldDeletedWhenIdExists(){

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
    @DisplayName("Delete deve retornar Not Found Exception quando ID não existe")
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
    @DisplayName("Delete deve retornar Integrity Violation Exception quando ID dependente")
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
