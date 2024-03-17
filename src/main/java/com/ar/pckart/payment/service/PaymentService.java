package com.ar.pckart.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.ar.pckart.admin.config.JwtUsersService;
import com.ar.pckart.payment.model.WalletTransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final WebClient webClient;
	private final JwtUsersService jwtUsersService;
	
	@Value("${payment.service.api.url.credit}")
	private String USER_SERVICE_URL_CREDIT_AMOUNT;
	
	public String creditToUserById(Long userId, WalletTransactionType walletTransactionType, Double amount) {
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(USER_SERVICE_URL_CREDIT_AMOUNT+userId)
				.queryParam("walletTransactionType", walletTransactionType)
				.queryParam("amount", amount);
		
		String uriPathWithQueryParams = uriBuilder.toUriString();

		return webClient.put()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtUsersService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, t-> t.createError())
				.bodyToMono(String.class)
				.block();
	}
	
	private String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}
	
}
