package com.ceyloncab.paymentmgtservice.external.repository;

import com.ceyloncab.paymentmgtservice.domain.entity.TripEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TripRepository extends MongoRepository<TripEntity,String> {
    Optional<TripEntity> findByTripId(String tripId);
}


