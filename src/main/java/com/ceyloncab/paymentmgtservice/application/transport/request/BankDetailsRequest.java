package com.ceyloncab.paymentmgtservice.application.transport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsRequest {
    @NotNull( message = "name not found for operation. This action is not allowed" )
    @Valid
    private String name;
    @NotNull( message = "bank not found for operation. This action is not allowed" )
    @Valid
    private String bank;
    @NotNull( message = "branch not found for operation. This action is not allowed" )
    @Valid
    private String branch;
    @NotNull( message = "accountNumber not found for operation. This action is not allowed" )
    @Valid
    private String accountNumber;
}
