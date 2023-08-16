package com.ceyloncab.paymentmgtservice.domain.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankDetail {
    @Indexed(unique = true)
    private String id;
    private String name;
    private String bank;
    private String branch;
    private String accountNumber;
}
