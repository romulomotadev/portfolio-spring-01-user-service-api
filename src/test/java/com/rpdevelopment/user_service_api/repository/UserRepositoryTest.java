package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.projection.UserAddressProjection;
import com.rpdevelopment.user_service_api.projection.UserDocumentProjection;
import com.rpdevelopment.user_service_api.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    //Garantir que a query realmente executo no banco
    @Autowired
    private TestEntityManager entityManager;

    private User user;

    //inicialização
    @BeforeEach
    void setUp() {
        //Criando user
        user = UserFactory.createNewUser();
    }

    //EMAIL EXISTENTE
    @Test
    public void shouldReturnTrueWhenEmailExists() {

        //Preparando
        User savedUser = userRepository.save(user);

        //Ação
        Boolean result = userRepository.existsByEmail(savedUser.getEmail());

        //Verificação
        Assertions.assertTrue(result);
        Assertions.assertEquals(savedUser.getEmail(), user.getEmail());
    }

    //POSSUI OUTRA ENTIDADE COM MESMO EMAIL
    @Test
    public void shouldReturnTrueWhenEmailExistsForAnotherId() {

        // 1. Criar e limpar o primeiro usuário
        User user1 = UserFactory.createNewUser();
        user1.setEmail("user1@gmail.com");
        user1.getPerson().setDocument("11122233344");
        userRepository.save(user1);

        // 2. Criar e limpar o segundo usuário
        User user2 = UserFactory.createNewUser();
        user2.setEmail("user2@gmail.com");
        user2.getPerson().setDocument("99988877766");
        userRepository.save(user2);

        // Ação
        boolean result = userRepository.existsByEmailAndIdNot("user1@gmail.com", user2.getId());

        // Verificação
        Assertions.assertTrue(result);
    }


    @Test
    public void searchUserDocumentShouldReturnPageOfProjections() {

        // 1. Preparando: EntityManager para persistir
        entityManager.persist(user.getPerson()); // Persiste a pessoa primeiro
        entityManager.persist(user);
        entityManager.flush(); // Empurra para o banco agora

        PageRequest pageRequest = PageRequest.of(0, 20);

        // 2. Ação: Chama a Query Nativa
        Page<UserDocumentProjection> result = userRepository.searchUserDocument(pageRequest);

        // 3. Verificação
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(11, result.getTotalElements());

        // Busca o registro específico dentro da lista retornada
        UserDocumentProjection foundProjection = result.getContent().stream()
                .filter(p -> p.getName().equals(user.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Usuário criado não encontrado na lista!"));

        // validamos os dados desse registro específico
        Assertions.assertEquals(user.getEmail(), foundProjection.getEmail());
        Assertions.assertEquals(user.getPerson().getDocument(), foundProjection.getDocument());
    }

    @Test
    public void searchUserAddressShouldReturnPageOfProjections() {

        // 1. Preparando
        entityManager.persist(user.getPerson());
        entityManager.persist(user);

        // IMPORTANTE: Como Address é uma lista, precisamos persistir cada um
        user.getAddresses().forEach(address -> {
            address.setUser(user); // Garante o vínculo do INNER JOIN
            entityManager.persist(address);
        });

        entityManager.flush();

        PageRequest pageRequest = PageRequest.of(0, 20);

        // 2. Ação
        Page<UserAddressProjection> result = userRepository.searchUserAddress(pageRequest);

        // Busca o registro específico dentro da lista retornada
        UserAddressProjection foundProjection = result.getContent().stream()
                .filter(p -> p.getRoad().equals(user.getAddresses().get(0).getRoad()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Endereço criado não encontrado na lista!"));

        // 3. Verificação0,
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(12, result.getTotalElements());
        Assertions.assertEquals(user.getAddresses().get(0).getRoad(), foundProjection.getRoad());
    }
}
