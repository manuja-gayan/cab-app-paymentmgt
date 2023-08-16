package com.ceyloncab.paymentmgtservice.application.transport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawAmountRequest {
    @NotNull( message = "withdrawAmount not found for operation. This action is not allowed" )
    @Valid
    private Double withdrawAmount;
    @NotNull( message = "currency not found for operation. This action is not allowed" )
    @Valid
    private String currency;
}
