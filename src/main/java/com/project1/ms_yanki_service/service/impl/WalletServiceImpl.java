package com.project1.ms_yanki_service.service.impl;

import com.project1.ms_yanki_service.config.CustomObjectMapper;
import com.project1.ms_yanki_service.exception.BadRequestException;
import com.project1.ms_yanki_service.model.domain.CreateWalletRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletResponse;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionRequest;
import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionResponse;
import com.project1.ms_yanki_service.model.mapper.WalletMapper;
import com.project1.ms_yanki_service.repository.WalletRepository;
import com.project1.ms_yanki_service.service.WalletService;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

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
    private KafkaTemplate<String, String> kafkaTemplate;

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
            .flatMap(req -> {
                try {
                    String message = objectMapper.objectToString(req);
                    kafkaTemplate.send(kafkaTopicName, message).get(3, TimeUnit.SECONDS);
                    return Single.just(req);
                } catch (Exception e) {
                    return Single.error(new BadRequestException("Error sending message to Kafka"));
                }
            })
            .map(e -> walletMapper.getCreateWalletTransactionResponse());
    }
}
