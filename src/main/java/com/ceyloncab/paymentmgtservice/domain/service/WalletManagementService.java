package com.ceyloncab.paymentmgtservice.domain.service;

import com.ceyloncab.paymentmgtservice.application.aop.AopConstants;
import com.ceyloncab.paymentmgtservice.application.transport.request.BankDetailsRequest;
import com.ceyloncab.paymentmgtservice.application.transport.request.WithdrawAmountRequest;
import com.ceyloncab.paymentmgtservice.domain.entity.DriverEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.DriverWalletEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.TransactionEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.wallet.DriverWalletDTO;
import com.ceyloncab.paymentmgtservice.domain.exception.DomainException;
import com.ceyloncab.paymentmgtservice.domain.service.assembler.impl.DriverWalletAssembler;
import com.ceyloncab.paymentmgtservice.domain.utils.BankDetail;
import com.ceyloncab.paymentmgtservice.domain.utils.Constants;
import com.ceyloncab.paymentmgtservice.domain.utils.UserType;
import com.ceyloncab.paymentmgtservice.domain.utils.WithdrawInfo;
import com.ceyloncab.paymentmgtservice.external.repository.DriverRepository;
import com.ceyloncab.paymentmgtservice.external.repository.DriverWalletRepository;
import com.ceyloncab.paymentmgtservice.external.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class WalletManagementService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverWalletRepository driverWalletRepository;

    @Autowired
    private DriverWalletAssembler driverWalletAssembler;

    @Autowired
    TransactionRepository transactionRepository;

    public CommonResponse<DriverWalletDTO> addBankDetails(BankDetailsRequest bankDetailsRequest){
    CommonResponse<DriverWalletDTO> response = new CommonResponse<>();

    DriverEntity driverEntity = driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
            .orElseThrow(() -> {
                log.error("Driver does not exist for given userId:{}", AopConstants.MDC_USERID);
                return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
            });

    try{
        Optional<DriverWalletEntity> byUserId = driverWalletRepository.findByUserId(driverEntity.getUserId());
        if (byUserId.isPresent()) {

            ArrayList<BankDetail> bankDetails = new ArrayList<>();
            BankDetail bankDetail = new BankDetail(
                    null,
                    bankDetailsRequest.getName(),
                    bankDetailsRequest.getBank(),
                    bankDetailsRequest.getBranch(),
                    bankDetailsRequest.getAccountNumber()
            );
            bankDetails.add(bankDetail);

            byUserId.get().setBankDetail(bankDetails);
            driverWalletRepository.save(byUserId.get());
            response.setData(driverWalletAssembler.toDto(byUserId.get()));
        }else {
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }

    }catch (Exception ex) {
        log.error("Error occurred in add bank details.Error:{}", ex.getMessage());
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
        return response;
    }
    response.setResponseHeader(new ResponseHeader(Constants.ResponseData.CREATE_SUCCESS));
    return response;
 }

    public CommonResponse  withdrawAmount(WithdrawAmountRequest withdrawAmountRequest){
        CommonResponse<Object> response = new CommonResponse<>();

        System.out.println(MDC.get(AopConstants.MDC_USERID));
        DriverEntity driverEntity = driverRepository.findById(MDC.get(AopConstants.MDC_USERID)).orElseThrow(() -> {
            log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
            return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
        });

        try {
            Optional<DriverWalletEntity> byUserId = driverWalletRepository.findByUserId(driverEntity.getUserId());
            if (byUserId.isPresent()) {

                if (byUserId.get().getWalletBalance()>= withdrawAmountRequest.getWithdrawAmount()){

                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setUserId(driverEntity.getUserId());
                    transactionEntity.setUserType(UserType.valueOf(MDC.get(AopConstants.CHANNEL)));
                    transactionEntity.setTransactionType("WITHDRAW");
                    transactionEntity.setWithdrawInfo(new WithdrawInfo(byUserId.get().getId(),"PENDING"));
                    transactionEntity.setTransactionStatus("PENDING");
                    transactionEntity.setAmount(withdrawAmountRequest.getWithdrawAmount());
                    transactionEntity.setClientRef(getClientRefId().toString());
                    transactionEntity.setRequestId(MDC.get(AopConstants.UUID));
                    transactionEntity.setCurrency(withdrawAmountRequest.getCurrency());
                    double value = byUserId.get().getWalletBalance() - withdrawAmountRequest.getWithdrawAmount();
                    byUserId.get().setWalletBalance(value);
                    driverWalletRepository.save(byUserId.get());
                    transactionRepository.save(transactionEntity);

                    response.setResponseHeader(new ResponseHeader(Constants.ResponseData.CREATE_SUCCESS));
                    return response;
                }
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
                return  response;

            }
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return  response;

        }catch (Exception ex) {
            log.error("Error occurred in create Driver Profile.Error:{}", ex.getMessage());
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }

    }

    public Long getClientRefId() {
        final Long LIMIT = 10000L;
        final Long deltaId = Long.parseLong(Long.toString(Long.parseLong((java.time.LocalDate.now()
                .format(DateTimeFormatter
                        .ofPattern("yyMMdd")))))
                .concat(Long.toString(System.currentTimeMillis() % LIMIT)));
        System.out.println("deltaId" + deltaId);
        return deltaId;

    }


}
