package com.ceyloncab.paymentmgtservice.external.repository;

import com.ceyloncab.paymentmgtservice.domain.entity.DriverWalletEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverWalletRepository extends MongoRepository<DriverWalletEntity,String> {

    Optional<DriverWalletEntity>findByUserId(String userId);
}
