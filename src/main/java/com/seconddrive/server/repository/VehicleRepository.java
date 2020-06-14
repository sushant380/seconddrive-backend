package com.seconddrive.server.repository;

import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.domain.Warehouse;

import java.math.BigDecimal;
import java.util.List;

public interface VehicleRepository {
    Vehicle findById(BigDecimal id);
    List<Vehicle>findAll();
    List<Vehicle>findByWarehouse(BigDecimal id);
    List<Vehicle>findByModel(String model);
    List<Vehicle>findByYear(BigDecimal year);
    List<Vehicle> findBySearchCriteria(SearchCriteria criteria);
}
