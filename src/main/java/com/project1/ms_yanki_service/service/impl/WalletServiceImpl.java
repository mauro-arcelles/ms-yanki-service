package com.project1.ms_yanki_service.service.impl;

import com.project1.ms_yanki_service.config.CustomObjectMapper;
import com.project1.ms_yanki_service.exception.BadRequestException;
import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import com.project1.ms_yanki_service.model.entity.Wallet;
import com.project1.ms_yanki_service.model.mapper.WalletMapper;
import com.project1.ms_yanki_service.repository.WalletRepository;
import com.project1.ms_yanki_service.service.KafkaProducer;
import com.project1.ms_yanki_service.service.WalletService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Value("${application.config.kafka.topic-name}")
    private String kafkaTopicName;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Override
    public Single<CreateWalletResponse> createWallet(Single<CreateWalletRequest> request) {
        return request
            .flatMap(req ->
                RxJava3Adapter.monoToSingle(walletRepository.findByDocumentNumber(req.getDocumentNumber())
                    .flatMap(
                        pc -> Mono.error(new BadRequestException("WALLET already exists for document number: " + req.getDocumentNumber())))
                    .then(Mono.just(req)))
            )
            .map(walletMapper::getCreateWalletEntity)
            .flatMap(wallet -> RxJava3Adapter.monoToSingle(walletRepository.save(wallet)))
            .map(walletMapper::getCreateWalletResponse);
    }

    @Override
    public Single<CreateWalletTransactionResponse> createWalletTransaction(Single<CreateWalletTransactionRequest> request) {
        return request
            .doOnSuccess(req -> {
                String message = objectMapper.objectToString(req);
                kafkaProducer.send(kafkaTopicName, message);
            })
            .doOnError(e -> {
                log.error("Error while sending message to kafka", e);
            })
            .map(e -> walletMapper.getCreateWalletTransactionResponse());
    }
}
