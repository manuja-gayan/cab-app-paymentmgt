package com.ceyloncab.paymentmgtservice.domain.entity;

import com.ceyloncab.paymentmgtservice.domain.utils.BankDetail;
import com.ceyloncab.paymentmgtservice.domain.utils.UserType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "DriverWallet")
public class DriverWalletEntity {
    @Id
    private String id;
    private Double walletBalance;
    private Double payableAmount;
    private ArrayList<BankDetail> bankDetail;
    private String userId;
    @LastModifiedDate
    private Date updatedTime;
}
