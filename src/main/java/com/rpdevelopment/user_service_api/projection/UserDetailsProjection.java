package com.rpdevelopment.user_service_api.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "Id", "username", "password", "Authority" })
public interface UserDetailsProjection {

    String getUsername();
    String getPassword();
    Long getId();
    String getAuthority();

}
