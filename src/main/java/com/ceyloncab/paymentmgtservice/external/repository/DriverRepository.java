package com.ceyloncab.paymentmgtservice.external.repository;

import com.ceyloncab.paymentmgtservice.domain.entity.DriverEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends MongoRepository<DriverEntity,String> {

}
