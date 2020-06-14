package com.seconddrive.server.service;


import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;

public interface VehicleService {
    VehicleResponse getAllVehicles();
    Vehicle getVehicleById(Long id);
    VehicleResponse searchVehicle(SearchCriteria criteria);
    VehicleResponse getByModel(String model);
}
