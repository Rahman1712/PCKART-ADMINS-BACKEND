package com.ar.pckart.admin.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ar.pckart.admin.model.Admin;
import com.ar.pckart.admin.repo.AdminRepository;
import com.ar.pckart.admin.service.AdminLoginException;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider   {
	
	@Autowired
	private AdminRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;  
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
	    String password = authentication.getCredentials().toString();
		Admin admin = repo.findByUsername(username).orElseThrow(()->
				new AdminLoginException("username not found"));
	    if (!encoder.matches(password, admin.getPassword())) {
	    	throw new AdminLoginException("Invalid Password");
	    }
	    List<String> userRoles = List.of(admin.getRole().name());
	    if(!admin.isEnabled()) throw new AdminLoginException("account is not enabled");
	    if(!admin.isNonLocked()) throw new AdminLoginException("account is locked");
	    
	    
	    return new UsernamePasswordAuthenticationToken(username, password, userRoles.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
