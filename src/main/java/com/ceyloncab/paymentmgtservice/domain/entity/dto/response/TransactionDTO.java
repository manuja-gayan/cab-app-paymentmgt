package com.ceyloncab.paymentmgtservice.domain.entity.dto.response;


import lombok.Data;

@Data
public class TransactionDTO {

    private String txnReference;
    private String responseCode;
    private String responseText;
    private boolean status=false;

}
