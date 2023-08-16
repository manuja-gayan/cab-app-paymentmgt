package com.ceyloncab.paymentmgtservice.application.transport.request;

import com.ceyloncab.paymentmgtservice.domain.utils.CompleteRequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteRequest {
    private String version;
    private String operation;
    private String msgId;
    private String requestDate;
    private Boolean validateOnly;
    private CompleteRequestData requestData;


}
