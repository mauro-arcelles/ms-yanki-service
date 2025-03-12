package com.project1.ms_yanki_service.service;

import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.server.ServerWebExchange;

public interface WalletService {
    Single<CreateWalletResponse> createWallet(Single<CreateWalletRequest> request, ServerWebExchange exchange);

    Single<CreateWalletTransactionResponse> createWalletTransaction(Single<CreateWalletTransactionRequest> request);
}
