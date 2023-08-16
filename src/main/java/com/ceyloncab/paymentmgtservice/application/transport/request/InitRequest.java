package com.ceyloncab.paymentmgtservice.application.transport.request;

import com.ceyloncab.paymentmgtservice.domain.utils.RequestData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitRequest {
    private String version;
    private String msgId;
    private String operation;
    private String requestDate;
    private Boolean validateOnly;
    private RequestData requestData;

}
