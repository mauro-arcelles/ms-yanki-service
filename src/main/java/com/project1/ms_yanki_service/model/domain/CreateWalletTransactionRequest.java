package com.project1.ms_yanki_service.model.domain;

import com.project1.ms_yanki_service.model.entity.WalletTransactionType;
import com.project1.ms_yanki_service.validation.EnumNamePattern;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateWalletTransactionRequest {
    @NotNull
    @NotEmpty
    private String originWalletId;

    @NotNull
    @NotEmpty
    private String destinationWalletId;

    @NotNull
    @DecimalMin("1.0")
    private BigDecimal amount;

    @EnumNamePattern(regexp = "DEPOSIT|TRANSFER")
    @NotNull
    private String type;
}
