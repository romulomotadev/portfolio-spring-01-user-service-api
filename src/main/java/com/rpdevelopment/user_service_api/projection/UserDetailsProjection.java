package com.rpdevelopment.user_service_api.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "Id", "username", "password", "Authority" })
@Schema(description = "Projection contendo informações do usuário e sua role para autenticação")
public interface UserDetailsProjection {

    @Schema(description = "Nome de usuário utilizado para login", example = "joao.silva")
    String getUsername();

    @Schema(description = "Senha do usuário (apenas para validação, normalmente não retornada no response)", example = "123456", accessMode = Schema.AccessMode.WRITE_ONLY)
    String getPassword();

    @Schema(description = "Identificador da role associada ao usuário", example = "1")
    Long getRoleId();

    @Schema(description = "Nome da autoridade/perfil do usuário", example = "ROLE_ADMIN")
    String getAuthority();

}
