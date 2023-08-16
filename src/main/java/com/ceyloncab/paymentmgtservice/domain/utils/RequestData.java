package com.ceyloncab.paymentmgtservice.domain.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestData {
    private String clientId;
    private String clientIdHash;
    private String transactionType;
    private TransactionAmount transactionAmount;
    private Redirect redirect;
    private String clientRef;
    private String comment;
    private Boolean tokenize;
    private String cssLocation1;
    private String cssLocation2;
    private Boolean useReliability;
    @JsonProperty("extraData ")
    private String extraData;
}
