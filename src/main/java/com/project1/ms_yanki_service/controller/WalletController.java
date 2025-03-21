package com.project1.ms_yanki_service.controller;

import com.project1.ms_yanki_service.model.domain.*;
import com.project1.ms_yanki_service.service.WalletService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/yanki/wallets")
@Tag(name = "Wallet", description = "Wallet management APIs")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Operation(summary = "Create a new wallet",
        description = "Creates a new wallet for a user",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateWalletRequest.class)
            )
        ))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            description = "Wallet created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CreateWalletResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public Single<ResponseEntity<CreateWalletResponse>> createWallet(@Valid @RequestBody Single<CreateWalletRequest> request, ServerWebExchange exchange) {
        return walletService.createWallet(request, exchange)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Operation(summary = "Create wallet transaction",
        description = "Creates a new transaction for a wallet",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateWalletTransactionRequest.class)
            )
        ))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            description = "Transaction message sent correctly",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CreateWalletTransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transaction")
    public Single<ResponseEntity<CreateWalletTransactionResponse>> createWalletTransaction(
        @Valid @RequestBody Single<CreateWalletTransactionRequest> request) {
        return walletService.createWalletTransaction(request)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @GetMapping("/{id}")
    public Single<ResponseEntity<GetWalletResponse>> getWallet(@PathVariable String id) {
        return walletService.getWalletById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/by-user-id/{userId}")
    public Single<ResponseEntity<GetWalletResponse>> getWalletByUserId(@PathVariable String userId) {
        return walletService.getWalletByUserId(userId).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Maybe<ResponseEntity<Void>> updateWallet(@PathVariable String id, @Valid @RequestBody Single<UpdateWalletRequest> request) {
        return walletService.updateWallet(id, request).map(v -> ResponseEntity.noContent().build());
    }
}
