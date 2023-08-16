package com.ceyloncab.paymentmgtservice.domain.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Redirect {
    private String returnUrl;
    private String cancelUrl;
    private String returnMethod;
}
