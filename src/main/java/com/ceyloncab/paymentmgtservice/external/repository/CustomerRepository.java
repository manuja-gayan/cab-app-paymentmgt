package com.ceyloncab.paymentmgtservice.external.repository;

import com.ceyloncab.paymentmgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerEntity,String> {

}
