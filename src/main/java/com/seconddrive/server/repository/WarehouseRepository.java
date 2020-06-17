package com.seconddrive.server.repository;

import com.seconddrive.server.domain.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Crud operations for warehouse repository
 */
@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, BigDecimal> {}
