package com.rpdevelopment.user_service_api.tests;

import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.User;

import java.time.LocalDate;

import static com.rpdevelopment.user_service_api.entity.Type.NATURAL_PERSON;

public class UserFactory {

    // Para Services (Mockito)
    // Já vem com IDs para simular objetos que "vieram do banco"
    public static User createValidUserWithId(){

        Person person = new Person(
                1L,
                "507.332.198-64",
                NATURAL_PERSON);

        User user = new User(
                1L,
                "Novo Usuario",
                "123456",
                "usuario@gmail.com",
                LocalDate.parse("2000-01-01"),
                person);

        person.setUser(user);

        Address address1 = new Address(1L, "Rua dos Bobos", "01", "Bairro", "Trabalho", "Cidade-MG", "00000-000");
        Address address2 = new Address(2L, "Rua A", "2A", "Bairro", "Casa", "Cidade-SP", "00000-000");

        user.addAddress(address1);
        user.addAddress(address2);

        return user;
    }

    // Para Repositories e Integração (@DataJpaTest)
    // O banco de dados vai gerar os IDs
    public static User createNewUser(){

        Person person = new Person(
                null,
                "507.332.198-64",
                NATURAL_PERSON);

        User user = new User(
                null,
                "Novo Usuario",
                "123456",
                "usuario@gmail.com",
                LocalDate.parse("2000-01-01"),
                person);

        person.setUser(user);

        Address address1 = new Address(null, "Rua dos Bobos", "01", "Bairro", "Trabalho", "Cidade-MG", "00000-000");
        Address address2 = new Address(null, "Rua A", "2A", "Bairro", "Casa", "Cidade-SP", "00000-000");

        user.addAddress(address1);
        user.addAddress(address2);

        return user;
    }
}
