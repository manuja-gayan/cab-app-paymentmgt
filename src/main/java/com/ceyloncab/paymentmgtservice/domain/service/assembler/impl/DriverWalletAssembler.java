package com.ceyloncab.paymentmgtservice.domain.service.assembler.impl;

import com.ceyloncab.paymentmgtservice.application.transport.request.PaymentRequest;
import com.ceyloncab.paymentmgtservice.domain.entity.DriverWalletEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.TransactionEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.wallet.DriverWalletDTO;
import com.ceyloncab.paymentmgtservice.domain.service.assembler.Assembler;
import org.springframework.stereotype.Service;


@Service
public class DriverWalletAssembler implements Assembler<DriverWalletEntity, DriverWalletDTO> {
    @Override
    public DriverWalletEntity fromDto(DriverWalletDTO dto) {
        return null;
    }

    @Override
    public DriverWalletDTO toDto(DriverWalletEntity model) {
        return null;
    }

    @Override
    public DriverWalletDTO toDto(DriverWalletEntity model, Object object) {
        return new DriverWalletDTO(model.getId(),model.getWalletBalance(),model.getPayableAmount(),model.getBankDetail(),
                model.getUserId(),model.getUpdatedTime());
    }

/*
    @Override
    public TransactionEntity fromDto(PaymentRequest dto) {
      *//*  TransactionEntity model = new TransactionEntity();
        model.setFirstName(dto.getFirstName());
        model.setLastName(dto.getLastName());
        model.setMsisdn(dto.getMsisdn());
        model.setLocation(dto.getLocation());*//*
        return model;
    }

    @Override
    public PaymentRequest toDto(TransactionEntity model) {
        return new PaymentRequest(model.getUserId(),model.getMsisdn(),model.getFirstName(),
                model.getLastName(),model.getLocation());
    }

    @Override
    public PaymentRequest toDto(TransactionEntity model, Object object) {
        return null;
    }*/
}
