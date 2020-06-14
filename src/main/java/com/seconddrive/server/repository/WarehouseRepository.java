package com.seconddrive.server.repository;

import com.seconddrive.server.domain.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, BigDecimal> {
}
