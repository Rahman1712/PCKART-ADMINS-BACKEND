package com.ar.pckart.payment.model;

import lombok.*;
import com.ar.pckart.user.model.UserDTO;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WalletDto {
	
    private Double amount;
    private WalletTransactionType walletTransactionType;
    private UserDTO user;
}
