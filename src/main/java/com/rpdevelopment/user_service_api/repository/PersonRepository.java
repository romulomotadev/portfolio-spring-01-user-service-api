package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    //DOCUMENT EXIST
    boolean existsByDocument(String document);

    //DOCUMENT EXIST - ID NOT
    boolean existsByDocumentAndIdNot(String document, Long id);
}
