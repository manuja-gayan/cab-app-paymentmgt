package com.ceyloncab.paymentmgtservice.external.repository;

import com.ceyloncab.paymentmgtservice.domain.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionEntity,String> {
    Optional<TransactionEntity> findByClientRefAndTransactionStatus(String clientRef,String transactionStatus);
    Optional<TransactionEntity> findByTripInfo_TripId(String tripId);

}
