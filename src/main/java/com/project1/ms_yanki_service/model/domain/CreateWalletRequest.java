package com.project1.ms_yanki_service.model.domain;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateWalletRequest {
    @NotNull
    @NotEmpty
    private String documentNumber;

    @NotNull
    @NotEmpty
    private String phoneNumber;

    @NotNull
    @NotEmpty
    private String phoneImei;

    @NotNull
    @NotEmpty
    @Email
    private String email;
}
