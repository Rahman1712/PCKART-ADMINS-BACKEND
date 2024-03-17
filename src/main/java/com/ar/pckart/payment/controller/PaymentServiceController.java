package com.ar.pckart.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.pckart.payment.model.WalletTransactionType;
import com.ar.pckart.payment.service.PaymentService;

@RestController
@RequestMapping("/api/v1/user-payment")
@PreAuthorize("hasAnyRole({'ADMIN','ADMINTRAINEE','EDITOR'})") //@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PaymentServiceController {

	@Autowired
	private PaymentService paymentService;
	
	@PutMapping("/credit/byUserId/{userId}")
	@PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<String> creditByUserId(
    		@PathVariable("userId") Long userId,
    		@RequestParam("amount") Double amount,
    		@RequestParam("walletTransactionType") WalletTransactionType walletTransactionType
    		){
        if (amount < 1){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("invalid amount");
        }
       
        return ResponseEntity.ok().body(paymentService.creditToUserById(userId, walletTransactionType, amount));
    }
}
