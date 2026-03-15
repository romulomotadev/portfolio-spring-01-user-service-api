package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class PersonRepositoryTest {

    //================== DEPENDÊNCIAS ==================

    @Autowired
    private PersonRepository personRepository;

    private User user;


    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() {
        user = UserFactory.createNewUser();
    }


    // ================= GET =================

    //DOCUMENTO EXISTENTE
    @Test
    @DisplayName("Deve retornar verdadeiro quando documento existe")
    public void shouldReturnTrueWhenDocumentExists(){

        //Preparando
        Person person = user.getPerson();
        Person save = personRepository.save(person);

        //Ação
        boolean result = personRepository.existsByDocument(person.getDocument());

        //Verificação
        Assertions.assertTrue(result);
        Assertions.assertEquals(person.getDocument(), save.getDocument());
    }


    //POSSUI OUTRA ENTIDADE COM MESMO DOCUMENTO
    @Test
    @DisplayName("Deve retornar verdadeiro quando documento existe para outro ID")
    public void shouldReturnTrueWhenDocumentExistsForAnotherId(){

        // 1. Criar e limpar o primeiro usuário
        User user1 = UserFactory.createNewUser();
        user1.setEmail("user1@gmail.com");
        user1.getPerson().setDocument("11122233344");
        personRepository.save(user1.getPerson());

        // 2. Criar e limpar o segundo usuário
        User user2 = UserFactory.createNewUser();
        user2.setEmail("user2@gmail.com");
        user2.getPerson().setDocument("99988877766");
        personRepository.save(user2.getPerson());

        // Ação
        boolean result = personRepository
                .existsByDocumentAndIdNot("11122233344", user2.getId());

        // Verificação
        Assertions.assertTrue(result);
    }
}
