package com.ceyloncab.paymentmgtservice.application.controller;

import com.ceyloncab.paymentmgtservice.application.transport.request.BankDetailsRequest;
import com.ceyloncab.paymentmgtservice.application.transport.request.WithdrawAmountRequest;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.wallet.DriverWalletDTO;
import com.ceyloncab.paymentmgtservice.domain.service.WalletManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("${base-url.context}/wallet")
public class DriverWalletController extends BaseController{

    @Autowired
    WalletManagementService walletManagementService;

    @PostMapping(value = "/add-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addBankDetails(@RequestBody(required = true) BankDetailsRequest bankDetailsRequest, HttpServletRequest request)  {
        CommonResponse<DriverWalletDTO> response = walletManagementService.addBankDetails(bankDetailsRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> withdrawAmount(@Validated @RequestBody WithdrawAmountRequest withdrawAmountRequest, HttpServletRequest request)  {
        CommonResponse response = walletManagementService.withdrawAmount(withdrawAmountRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }


}
