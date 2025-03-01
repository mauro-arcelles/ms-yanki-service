package com.project1.ms_yanki_service.model.mapper;

import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import com.project1.ms_yanki_service.model.entity.Wallet;
import com.project1.ms_yanki_service.model.entity.WalletStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
