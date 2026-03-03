package com.rpdevelopment.user_service_api.tests;

import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.User;

import static com.rpdevelopment.user_service_api.tests.UserFactory.*;


public class UserFactoryDto {

    //USER
    public static UserPersonAddressDto createUserFactoryDto(){
        User user = createUser();
        return new UserPersonAddressDto(user);
    }

    //EMAIL DUPLICADO
    public static UserPersonAddressDto createUserEmailDuplicateFactoryDto() {
        User userEmailDuplicate = createUserEmailDuplicate();
        return new UserPersonAddressDto(userEmailDuplicate);
    }

    //DOCUMENTO DUPLICADO
    public static UserPersonAddressDto createUserDocumentDuplicateFactoryDto() {
        User userDocumentDuplicate = createUserDocumentDuplicate();
        return new UserPersonAddressDto(userDocumentDuplicate);
    }

}
