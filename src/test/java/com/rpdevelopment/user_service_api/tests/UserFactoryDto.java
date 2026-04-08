package com.rpdevelopment.user_service_api.tests;

import com.rpdevelopment.user_service_api.dto.users.UserPersonAddressDTO;
import com.rpdevelopment.user_service_api.entity.User;

import static com.rpdevelopment.user_service_api.tests.UserFactory.*;


public class UserFactoryDto {

    // Para Services (Mockito)
    public static UserPersonAddressDTO createValidUserFactoryDto(){
        User userValid  = createValidUserWithId();
        return new UserPersonAddressDTO(userValid);
    }

    // Para Repositories e Integração (@DataJpaTest)
    public static UserPersonAddressDTO createNewUserFactoryDto(){
        User newUser = createNewUser();
        return new UserPersonAddressDTO(newUser);
    }
}
