package com.rpdevelopment.user_service_api.service;


import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.ForbiddenException;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	@Autowired
	private UserRepository repository;


	//BLOQUEAR ACESSO SE NÃO FOR ADMINISTRADOR OU DONO DA REQUISIÇÃO (FIND BY ID)
	public void validateSelfOrAdmin(long userId) {
		User me = authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
	}


	//BUSCA DE USUÁRIO AUTENTICADO
	protected User authenticated(){
		try{

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
			return repository.findByEmail(username).get();

		} catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
		}
	}

	@Transactional(readOnly = true)
	public UserPersonAddressDto getMe(){
		User user = authenticated();
		return new UserPersonAddressDto(user);
	}
}
