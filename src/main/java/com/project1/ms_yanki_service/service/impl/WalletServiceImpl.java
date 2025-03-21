package com.project1.ms_yanki_service.service.impl;

import com.project1.ms_yanki_service.config.CustomObjectMapper;
import com.project1.ms_yanki_service.config.auth.SecurityUtils;
import com.project1.ms_yanki_service.exception.BadRequestException;
import com.project1.ms_yanki_service.model.domain.*;
import com.project1.ms_yanki_service.model.entity.WalletTransactionType;
import com.project1.ms_yanki_service.model.mapper.WalletMapper;
import com.project1.ms_yanki_service.repository.WalletRepository;
import com.project1.ms_yanki_service.service.WalletService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Mono;

import java.util.Optional;
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

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public Single<CreateWalletResponse> createWallet(Single<CreateWalletRequest> request, ServerWebExchange exchange) {
        return request
            .flatMap(req ->
                RxJava3Adapter.monoToSingle(walletRepository.findByDocumentNumber(req.getDocumentNumber())
                    .flatMap(
                        pc -> Mono.error(new BadRequestException("WALLET already exists for document number: " + req.getDocumentNumber())))
                    .then(Mono.just(req)))
            )
            .map(walletMapper::getCreateWalletEntity)
            .flatMap(wallet -> securityUtils.getUserIdFromToken(exchange)
                .flatMap(userId ->
                    RxJava3Adapter.monoToSingle(walletRepository.findByUserId(userId)
                        .flatMap(e -> Mono.error(new BadRequestException("The user already has a wallet")))
                        .then(Mono.just(userId)))
                )
                .map(userId -> {
                    wallet.setUserId(userId);
                    return wallet;
                })
            )
            .flatMap(wallet -> RxJava3Adapter.monoToSingle(walletRepository.save(wallet)))
            .map(walletMapper::getCreateWalletResponse);
    }

    @Override
    public Single<CreateWalletTransactionResponse> createWalletTransaction(Single<CreateWalletTransactionRequest> request) {
        return request
            .flatMap(req -> {
                if (req.getOriginWalletId().equals(req.getDestinationWalletId())) {
                    return Single.error(new BadRequestException("Origin and destination WALLET ids should be different"));
                }
                return RxJava3Adapter.monoToSingle(walletRepository.findById(req.getOriginWalletId())
                    .switchIfEmpty(Mono.error(new BadRequestException("Origin WALLET not found with id " + req.getOriginWalletId())))
                    .flatMap(originWallet -> {
                        if (WalletTransactionType.TRANSFER.toString().equals(req.getType())) {
                            if (originWallet.getBalance().compareTo(req.getAmount()) < 0) {
                                return Mono.error(new BadRequestException("Insufficient balance in origin WALLET"));
                            }
                        }
                        return walletRepository.findById(req.getDestinationWalletId());
                    })
                    .switchIfEmpty(Mono.error(new BadRequestException("Destination WALLET not found with id " + req.getDestinationWalletId())))
                    .flatMap(destionationWallet -> Mono.just(req)));
            })
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

    @Override
    public Single<GetWalletResponse> getWalletById(String walletId) {
        return RxJava3Adapter.monoToSingle(walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new BadRequestException("WALLET not found with id " + walletId))))
            .map(walletMapper::getGetWalletResponse);
    }

    @Override
    public Maybe<Void> updateWallet(String walletId, Single<UpdateWalletRequest> request) {
        return request.flatMapMaybe(req -> {
            return RxJava3Adapter.monoToMaybe(walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new BadRequestException("WALLET not found with id " + walletId)))
                .flatMap(wallet -> {
                    Optional.ofNullable(req.getBalance()).ifPresent(wallet::setBalance);
                    return walletRepository.save(wallet);
                })
                .then());
        });
    }

    @Override
    public Single<GetWalletResponse> getWalletByUserId(String userId) {
        return RxJava3Adapter.monoToSingle(walletRepository.findByUserId(userId)
            .switchIfEmpty(Mono.error(new BadRequestException("WALLET not found with userId " + userId)))
            .map(walletMapper::getGetWalletResponse));
    }
}
