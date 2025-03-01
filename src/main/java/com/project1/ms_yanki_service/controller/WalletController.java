package com.project1.ms_yanki_service.controller;

import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import com.project1.ms_yanki_service.service.WalletService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public Single<ResponseEntity<CreateWalletResponse>> createWallet(@Valid @RequestBody Single<CreateWalletRequest> request) {
        return walletService.createWallet(request).map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @PostMapping("/transaction")
    public Single<ResponseEntity<CreateWalletTransactionResponse>> createWalletTransaction(@Valid @RequestBody Single<CreateWalletTransactionRequest> request) {
        return walletService.createWalletTransaction(request).map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }
}
