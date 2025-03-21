package com.project1.ms_yanki_service.model.mapper;

import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import com.project1.ms_yanki_service.model.domain.GetWalletResponse;
import com.project1.ms_yanki_service.model.entity.Wallet;
import com.project1.ms_yanki_service.model.entity.WalletStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class WalletMapper {

    @Autowired
    private Clock clock;

    public Wallet getCreateWalletEntity(CreateWalletRequest request) {
        return Wallet.builder()
            .documentNumber(request.getDocumentNumber())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .phoneImei(request.getPhoneImei())
            .creationDate(LocalDateTime.now(clock))
            .status(WalletStatus.ACTIVE)
            .balance(BigDecimal.ZERO)
            .build();
    }

    public CreateWalletResponse getCreateWalletResponse(Wallet wallet) {
        CreateWalletResponse response = new CreateWalletResponse();
        response.setId(wallet.getId());
        response.setDocumentNumber(wallet.getDocumentNumber());
        response.setEmail(wallet.getEmail());
        response.setPhoneImei(wallet.getPhoneImei());
        response.setPhoneNumber(wallet.getPhoneNumber());
        return response;
    }

    public CreateWalletTransactionResponse getCreateWalletTransactionResponse() {
        CreateWalletTransactionResponse response = new CreateWalletTransactionResponse();
        response.setMessage("Transaction message sent correctly");
        return response;
    }

    public GetWalletResponse getGetWalletResponse(Wallet wallet) {
        GetWalletResponse response = new GetWalletResponse();
        response.setId(wallet.getId());
        response.setDocumentNumber(wallet.getDocumentNumber());
        response.setEmail(wallet.getEmail());
        response.setPhoneImei(wallet.getPhoneImei());
        response.setPhoneNumber(wallet.getPhoneNumber());
        response.setBalance(wallet.getBalance());
        response.setCreationDate(wallet.getCreationDate());
        response.setStatus(wallet.getStatus());
        response.setUserId(wallet.getUserId());
        return response;
    }
}
