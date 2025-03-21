package com.project1.ms_yanki_service.model.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateWalletRequest {
    private BigDecimal balance;
}
