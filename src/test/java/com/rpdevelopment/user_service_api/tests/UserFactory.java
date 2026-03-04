package com.rpdevelopment.user_service_api.tests;

import com.rpdevelopment.user_service_api.entity.Address;
import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.User;

import java.time.LocalDate;

import static com.rpdevelopment.user_service_api.entity.Type.NATURAL_PERSON;

public class UserFactory {

    public static User createUser(){
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

        Address address1 = new Address(2L, "Rua dos Bobos", "01", "Bairro", "Trabalho", "Cidade-MG", "00000-000");
        Address address2 = new Address(3L, "Rua A", "2A", "Bairro", "Casa", "Cidade-SP", "00000-000");

        user.addAddress(address1);
        user.addAddress(address2);

        return user;
    }
}
