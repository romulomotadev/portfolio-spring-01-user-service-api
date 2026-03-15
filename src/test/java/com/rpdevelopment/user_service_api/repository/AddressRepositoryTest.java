package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class AddressRepositoryTest {

    //================== DEPENDÊNCIAS ==================

    @Autowired
    private AddressRepository addressRepository;

    private User user;


    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() {
        user = UserFactory.createValidUserWithId();
    }


    //================== GET ==================

    @Test
    @DisplayName("Busca deve buscar endereço por campo")
    public void shouldFindAddressByFields(){

        //PREPARANDO
        Address address = user.getAddresses().getFirst();
        addressRepository.save(address);

        //Ação
        Optional<Address> result = addressRepository
                .findByRoadAndNumberAndZipCodeAndCity(
                        address.getRoad(),
                        address.getNumber(),
                        address.getZipCode(),
                        address.getCity());

        //VERIFICAÇÃO
        Assertions.assertTrue(result.isPresent());

        Address foundAddress = result.get();

        Assertions.assertEquals("Rua dos Bobos", foundAddress.getRoad());
        Assertions.assertEquals("01", foundAddress.getNumber());
        Assertions.assertEquals("00000-000", foundAddress.getZipCode());
        Assertions.assertEquals("Cidade-MG", foundAddress.getCity());
    }
}
