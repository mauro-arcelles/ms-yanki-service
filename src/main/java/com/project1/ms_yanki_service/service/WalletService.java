package com.project1.ms_yanki_service.service;

import com.project1.ms_yanki_service.model.domain.*;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.web.server.ServerWebExchange;

public interface WalletService {
    Single<CreateWalletResponse> createWallet(Single<CreateWalletRequest> request, ServerWebExchange exchange);

    Single<CreateWalletTransactionResponse> createWalletTransaction(Single<CreateWalletTransactionRequest> request);

    Single<GetWalletResponse> getWalletById(String walletId);

    Maybe<Void> updateWallet(String walletId, Single<UpdateWalletRequest> request);
}
