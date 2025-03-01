package com.project1.ms_yanki_service.model.domain;

import lombok.Data;

@Data
public class CreateWalletResponse {
    private String id;

    private String documentNumber;

    private String phoneNumber;

    private String phoneImei;

    private String email;
}
