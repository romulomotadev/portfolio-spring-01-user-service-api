package com.rpdevelopment.user_service_api.service;


import com.rpdevelopment.user_service_api.dto.UserPersonAddressDto;
import com.rpdevelopment.user_service_api.entity.Role;
import com.rpdevelopment.user_service_api.entity.User;
import com.rpdevelopment.user_service_api.exception.ForbiddenException;
import com.rpdevelopment.user_service_api.exception.ResourceNotFoundException;
import com.rpdevelopment.user_service_api.repository.RoleRepository;
import com.rpdevelopment.user_service_api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	// =============== DEPENDÊNCIAS ====================

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;


	// ========= CONSTRUTOR DEPENDÊNCIAS ===============

	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}


	// =============== GET ====================

	//RECUPERA OS DADOS DO USUÁRIO AUTENTICADO

/*	Utiliza o metodo authenticated() para recuperar o usuário
	logado e converte a entidade para UserPersonAddressDto.*/

	@Transactional(readOnly = true)
	public UserPersonAddressDto getMe(){
		User user = authenticated();
		return new UserPersonAddressDto(user);
	}


	// =============== ROLE ====================

	//ADICIONA ROLE A UM USUÁRIO EXISTENTE
	@Transactional
	public void addRoleToUser(Long userId, Long roleId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		user.getRoles().add(role);
	}


	//REMOVE ROLE DE UM USUÁRIO EXISTENTE
	@Transactional
	public void removeRoleToUser(Long userId, Long roleId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		user.getRoles().remove(role);
	}


	// =============== MÉTODOS ====================

	//BLOQUEAR ACESSO NÃO ADMIN OU USER AUTENTICADO (FIND BY ID)
	public void validateSelfOrAdmin(long userId) {
		User me = authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
	}


	//RECUPERA O USUÁRIO ATUALMENTE AUTENTICADO

/*	 Este metodo acessa o SecurityContext do Spring Security,
	 extrai o JWT do usuário autenticado e obtém o "username"
	 armazenado no token. Em seguida busca o usuário no banco
	 de dados pelo e-mail. */

	protected User authenticated(){
		try{

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
			return userRepository.findByEmail(username).get();

		} catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
		}
	}
}
