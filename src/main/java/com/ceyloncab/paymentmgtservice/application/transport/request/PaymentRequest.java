package com.ceyloncab.paymentmgtservice.application.transport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull( message = "paymentAmount not found for operation. This action is not allowed" )
    @Valid
    private Double paymentAmount;
    @NotNull( message = "currency not found for operation. This action is not allowed" )
    @Valid
    private String currency;
    @NotNull( message = "transactionType not found for operation. This action is not allowed" )
    @Valid
    private String transactionType;
    private String tripId;
}
