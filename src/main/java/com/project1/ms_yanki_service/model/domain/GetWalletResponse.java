package com.project1.ms_yanki_service.model.domain;

import com.project1.ms_yanki_service.model.entity.WalletStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GetWalletResponse {
    private String id;

    private String documentNumber;

    private String phoneNumber;

    private String phoneImei;

    private String email;

    private WalletStatus status;

    private LocalDateTime creationDate;

    private String userId;

    private BigDecimal balance;
}
