package com.rpdevelopment.user_service_api.tests;

import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.User;

import static com.rpdevelopment.user_service_api.tests.UserFactory.*;


public class UserFactoryDto {

    // Para Services (Mockito)
    public static UserPersonAddressDto createValidUserFactoryDto(){
        User userValid  = createValidUserWithId();
        return new UserPersonAddressDto(userValid);
    }

    // Para Repositories e Integração (@DataJpaTest)
    public static UserPersonAddressDto createNewUserFactoryDto(){
        User newUser = createNewUser();
        return new UserPersonAddressDto(newUser);
    }
}
