package com.ceyloncab.paymentmgtservice.domain.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAmount {
    private Long totalAmount;
    private Long paymentAmount;
    private Long serviceFeeAmount;
    private String currency;
}
