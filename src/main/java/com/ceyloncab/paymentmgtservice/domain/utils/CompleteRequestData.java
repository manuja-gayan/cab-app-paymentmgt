package com.ceyloncab.paymentmgtservice.domain.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteRequestData {
    private String clientId;
    private String reqid;

}
