package com.rpdevelopment.user_service_api.repository;

import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.projection.UserAddressProjection;
import com.rpdevelopment.user_service_api.projection.UserDetailsProjection;
import com.rpdevelopment.user_service_api.projection.UserDocumentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //SEARCH USER DOCUMENT
    @Query(nativeQuery = true, value = "SELECT TB_USER.NAME, TB_USER.EMAIL, TB_PERSON.DOCUMENT " +
            "FROM TB_USER " +
            "INNER JOIN TB_PERSON ON TB_USER.PERSON_ID = TB_PERSON.ID ",
            countQuery = "SELECT COUNT(*) " +
                    "FROM TB_USER " +
                    "INNER JOIN TB_PERSON ON TB_USER.PERSON_ID = TB_PERSON.ID")
    Page<UserDocumentProjection> searchUserDocument(Pageable pageable);

    //SEARCH USER ADDRESS
    @Query(nativeQuery = true, value = "SELECT TB_USER.NAME, TB_USER.EMAIL, " +
            "TB_ADDRESS.ROAD, TB_ADDRESS.NUMBER, TB_ADDRESS.NEIGHBORHOOD, TB_ADDRESS.COMPLEMENT, TB_ADDRESS.CITY, TB_ADDRESS.ZIP_CODE " +
            "FROM TB_USER " +
            "INNER JOIN TB_ADDRESS ON TB_ADDRESS.USER_ID = TB_USER.ID ",
            countQuery = "SELECT COUNT(*) " +
            "FROM TB_USER " +
            "INNER JOIN TB_ADDRESS ON TB_ADDRESS.USER_ID = TB_USER.ID")
    Page<UserAddressProjection> searchUserAddress(Pageable pageable);


    //EMAIL EXIST
    boolean existsByEmail(String email);

    //EMAIL EXIST - ID NOT
    boolean existsByEmailAndIdNot(String email, Long id);

    //BUSCA DE AUTORIZAÇÃO DO USUÁRIO
/*    @Query(nativeQuery = true, value = "SELECT TB_USER.EMAIL AS USERNAME, " +
            "TB_USER.PASSWORD, TB_ROLE.ID AS ROLEID, TB_ROLE.AUTHORITY " +
            "FROM TB_USER " +
            "INNER JOIN TB_USER_ROLE ON TB_USER.ID = TB_USER_ROLE.USER_ID " +
            "INNER JOIN TB_ROLE ON TB_ROLE.ID = TB_USER_ROLE.ROLE_ID " +
            "WHERE TB_USER.EMAIL = :email")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);*/

    @Query(nativeQuery = true, value = """
            SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
            FROM tb_user
            INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
            INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
            WHERE tb_user.email = :email
        """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
