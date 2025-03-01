package com.project1.ms_yanki_service.service.impl;

import com.project1.ms_yanki_service.exception.BadRequestException;
import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import com.project1.ms_yanki_service.model.mapper.WalletMapper;
import com.project1.ms_yanki_service.repository.WalletRepository;
import com.project1.ms_yanki_service.service.KafkaProducer;
import com.project1.ms_yanki_service.service.WalletService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;

@Service
public class WalletServiceImpl implements WalletService {

    @Value("${application.config.kafka.topic-name}")
    private String kafkaTopicName;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public Single<CreateWalletResponse> createWallet(Single<CreateWalletRequest> request) {
        return request
            .flatMap(req ->
                RxJava3Adapter.monoToMaybe(walletRepository.findByDocumentNumber(req.getDocumentNumber()))
                    .switchIfEmpty(Single.error(new BadRequestException("documentNumber already exists")))
                    .map(e -> req)
            )
            .map(walletMapper::getCreateWalletEntity)
            .flatMap(wallet -> RxJava3Adapter.monoToSingle(walletRepository.save(wallet)))
            .map(walletMapper::getCreateWalletResponse);
    }

    @Override
    public Single<CreateWalletTransactionResponse> createWalletTransaction(Single<CreateWalletTransactionRequest> request) {
        return request
            .doOnSuccess(req -> kafkaProducer.send(kafkaTopicName, req))
            .map(e -> walletMapper.getCreateWalletTransactionResponse());
    }
}
