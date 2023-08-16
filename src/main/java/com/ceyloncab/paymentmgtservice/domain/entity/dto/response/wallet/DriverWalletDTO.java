package com.ceyloncab.paymentmgtservice.domain.entity.dto.response.wallet;

import com.ceyloncab.paymentmgtservice.domain.utils.BankDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverWalletDTO {
    private String id;
    private Double walletBalance;
    private Double payableAmount;
    private List<BankDetail> bankDetail;
    private String userId;
    private Date updatedTime;
}
