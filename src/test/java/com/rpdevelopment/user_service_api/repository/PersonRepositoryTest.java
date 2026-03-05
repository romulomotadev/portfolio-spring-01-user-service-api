package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.Person;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    //DOCUMENTO EXISTENTE
    @Test
    public void shouldReturnTrueWhenDocumentExists(){

        //Preparando
        User user = UserFactory.createUser();
        Person person = user.getPerson();
        Person save = personRepository.save(person);

        //Ação
        Boolean result = personRepository.existsByDocument(person.getDocument());

        //Verificação
        Assertions.assertTrue(result);
        Assertions.assertEquals(person.getDocument(), save.getDocument());
    }

    //POSSUI OUTRA ENTIDADE COM MESMO DOCUMENTO
    @Test
    public void shouldReturnTrueWhenDocumentExistsForAnotherId(){

        // Preparando
        User user = UserFactory.createUser();
        Person person = personRepository.save(user.getPerson());

        User user2 = UserFactory.createUser();
        user2.setId(2L);

        // Ação
        Boolean result = personRepository
                .existsByDocumentAndIdNot(user2.getPerson().getDocument(), user2.getId());

        // Verificação
        Assertions.assertTrue(result);
    }
}
