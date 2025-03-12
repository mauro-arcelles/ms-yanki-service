package com.project1.ms_yanki_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wallets")
public class Wallet {
    @Id
    private String id;

    private String documentNumber;

    private String phoneNumber;

    private String phoneImei;

    private String email;

    private WalletStatus status;

    private LocalDateTime creationDate;

    private String userId;
}
