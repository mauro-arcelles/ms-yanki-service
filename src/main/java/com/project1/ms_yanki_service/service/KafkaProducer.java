package com.project1.ms_yanki_service.service;

import com.project1.ms_yanki_service.model.domain.CreateWalletTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, CreateWalletTransactionRequest> kafkaTemplate;

    public void send(String topic, CreateWalletTransactionRequest message) {
        kafkaTemplate.send(topic, message);
    }
}
