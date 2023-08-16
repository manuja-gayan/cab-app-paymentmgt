package com.ceyloncab.paymentmgtservice.domain.entity;

import com.ceyloncab.paymentmgtservice.domain.utils.BankDetail;
import com.ceyloncab.paymentmgtservice.domain.utils.TripInfo;
import com.ceyloncab.paymentmgtservice.domain.utils.UserType;
import com.ceyloncab.paymentmgtservice.domain.utils.WithdrawInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Transaction")
public class TransactionEntity {
    @Id
    private String id;
    private String userId;
    private UserType userType;
    private String transactionType;
    private TripInfo tripInfo;
    private WithdrawInfo withdrawInfo;
    private String transactionStatus;
    private Double amount;
    private String clientRef;
    private String requestId;
    private String currency;
    @LastModifiedDate
    private Date updatedTime;
}
