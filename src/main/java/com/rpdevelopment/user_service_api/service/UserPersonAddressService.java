package com.rpdevelopment.user_service_api.service;

import com.rpdevelopment.user_service_api.dto.AddressDto;
import com.rpdevelopment.user_service_api.dto.PersonDto;
import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.Role;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.DuplicateResourceException;
import com.rpdevelopment.user_service_api.exception.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.projection.UserAddressProjection;
import com.rpdevelopment.user_service_api.projection.UserDetailsProjection;
import com.rpdevelopment.user_service_api.projection.UserDocumentProjection;
import com.rpdevelopment.user_service_api.repository.AddressRepository;
import com.rpdevelopment.user_service_api.repository.PersonRepository;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPersonAddressService implements UserDetailsService {

    //================ DEPENDÊNCIAS =====================

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final UserService authService;
    private final PasswordEncoder passwordEncoder;


    //================ CONSTRUTOR =====================

    public UserPersonAddressService(UserRepository userRepository, AddressRepository addressRepository,
                                    PersonRepository personRepository, UserService authService,
                                    PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }


    //================ GET =====================

    // FIND ALL
    @Transactional(readOnly = true)
    public Page<UserPersonAddressDto> usersFindAll (Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserPersonAddressDto::new); }


    //FIND BY ID
    @Transactional(readOnly = true)
    public UserPersonAddressDto usersFindById (Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Id Not Found"));

        //Bloquear user não admin, e user diferente do logado
        authService.validateSelfOrAdmin(user.getId());
        return new UserPersonAddressDto(user);
    }


    //================ QUERY USER DOCUMENT =====================

    @Transactional(readOnly = true)
    public Page<UserDocumentProjection> searchUserDocument(Pageable pageable) {
        return userRepository.searchUserDocument(pageable);
    }


    //================ QUERY USER ADDRESS =====================

    @Transactional(readOnly = true)
    public Page<UserAddressProjection> searchUserAddress(Pageable pageable) {
        return userRepository.searchUserAddress(pageable);
    }


    //================ SAVE =====================

    @Transactional
    public UserPersonAddressDto save (UserPersonAddressDto userPersonAddressDto) {

        //Email existe no banco?
        if (userRepository.existsByEmail(userPersonAddressDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        //Documento existe no banco?
        if (personRepository.existsByDocument(userPersonAddressDto.getPerson().getDocument())){
            throw new DuplicateResourceException("Document already exists");
        }

        //Preparando User
        User user = new User();
        copyUserDtoToUser(userPersonAddressDto, user);
        // criptografa senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        //Preparando Person
        Person person = new Person();
        copyPersonDtoToPerson(userPersonAddressDto, person);
        user.setPerson(person);

        //Preparando Addresses
        if (userPersonAddressDto.getAddresses() != null){
            for(AddressDto addressDto : userPersonAddressDto.getAddresses()){

                Address address = new Address();
                copyAddressDtoToAddress(addressDto, address);
                user.addAddress(address); }
            }

        User savedUser = userRepository.save(user);
        return new UserPersonAddressDto(savedUser); }


    //================ UPDATE =====================

    //UPDATE ADDRESSES
    @Transactional
    public List<AddressDto> updateAddresses(List<AddressDto> addressDtoList, Long id) {

        //Endereço repository
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Address> addresses = user.getAddresses();


        for (AddressDto addressDto : addressDtoList) {

            if (addressDto.getId() != null) {
                // Atualiza endereço existente
                Address address = addressRepository.getReferenceById(addressDto.getId());
                copyAddressDtoToAddress(addressDto, address);

            } else {
                // Cria novo endereço
                Address newAddress = new Address();
                copyAddressDtoToAddress(addressDto, newAddress);
                user.addAddress(newAddress);
            }
        }

        userRepository.save(user);
        return user.getAddresses().stream().map(AddressDto::new).toList();
    }


    //UPDATE PERSON
    @Transactional
    public PersonDto updatePerson(PersonDto personDto, Long id) {

        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Id Not Found"));

        Person person = user.getPerson();

        person.setDocument(personDto.getDocument());
        person.setType(personDto.getType());

        user.setPerson(person);
        userRepository.save(user);

        return new PersonDto(person);
    }


    //UPDATE ALL
    @Transactional
    public UserPersonAddressDto update(UserPersonAddressDto userPersonAddressDto, Long id) {

        //User existe no banco?
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));

        //E-mail no banco?
        if (userRepository.existsByEmailAndIdNot(userPersonAddressDto.getEmail(), id)) {
            throw new DuplicateResourceException("Email already exists");
        }

        //Documento existe no banco?
        if (personRepository.existsByDocumentAndIdNot(userPersonAddressDto.getPerson().getDocument(), id)){
            throw new DuplicateResourceException("Document already exists");
        }

        copyUserDtoToUser(userPersonAddressDto, user);
        // criptografa senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Preparando Person
        Person person = user.getPerson();
        if (person == null) {
            person = new Person();
        }
        copyPersonDtoToPerson(userPersonAddressDto, person);
        user.setPerson(person);

        // Preparando addresses
        user.getAddresses().clear();
        if (userPersonAddressDto.getAddresses() != null) {

            for (AddressDto addressDto : userPersonAddressDto.getAddresses()) {
                Address address;

                //Novo endereço, caso não exista no banco
                if (addressDto.getId() != null) {
                    address = addressRepository.getReferenceById(addressDto.getId());
                    copyAddressDtoToAddress(addressDto, address);

                } else {
                    Optional<Address> existingAddress = addressRepository.findByRoadAndNumberAndZipCodeAndCity(
                            addressDto.getRoad(),
                            addressDto.getNumber(),
                            addressDto.getZipCode(),
                            addressDto.getCity());

                    if (existingAddress.isPresent()) {
                        // reutiliza endereço existente
                        address = existingAddress.get();

                    } else {
                        // cria novo
                        address = new Address();
                        copyAddressDtoToAddress(addressDto, address);
                    }
                }
                user.addAddress(address);
            }
        }
        User savedUser = userRepository.save(user);
        return new UserPersonAddressDto(savedUser);
    }


    //================ DELETE =====================

    @Transactional
    public void delete (Long id) {

        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Id Not Found"));
        userRepository.delete(user);

    }


    //================ MÉTODOS =====================

    // Converte dto user para entity user
    public void copyUserDtoToUser(UserPersonAddressDto userPersonAddressDto, User user) {
        user.setName(userPersonAddressDto.getName());
        user.setEmail(userPersonAddressDto.getEmail());
        user.setBirthDate(userPersonAddressDto.getBirthDate());
        user.setPassword(userPersonAddressDto.getPassword()); }

    //Converter dto person para entity person
    public void copyPersonDtoToPerson(UserPersonAddressDto userPersonAddressDto, Person person) {
        person.setDocument(userPersonAddressDto.getPerson().getDocument());
        person.setType(userPersonAddressDto.getPerson().getType()); }

    //Converte dto Address para entity address
    public void copyAddressDtoToAddress(AddressDto addressDto, Address address) {
        address.setRoad(addressDto.getRoad());
        address.setNumber(addressDto.getNumber());
        address.setNeighborhood(addressDto.getNeighborhood());
        address.setComplement(addressDto.getComplement());
        address.setCity(addressDto.getCity());
        address.setZipCode(addressDto.getZipCode());
    }


    //================ SEGURANÇA (User Details Service) =====================

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User();
        user.setEmail(result.getFirst().getUsername());
        user.setPassword(result.getFirst().getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}
