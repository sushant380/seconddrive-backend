package com.seconddrive.server.service.impl;

import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;
import com.seconddrive.server.repository.VehicleRepository;
import com.seconddrive.server.service.VehicleService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
  @Inject VehicleRepository vehicleRepository;

  @Override
  public VehicleResponse getAllVehicles() {
    List<Vehicle> allVehicles = vehicleRepository.findAll();
    return VehicleResponse.builder()
        .vehicles(allVehicles)
        .total(allVehicles.size())
        .page(1)
        .build();
  }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(BigDecimal.valueOf(id));
    }

    @Override
    public VehicleResponse searchVehicle(SearchCriteria criteria) {
        List<Vehicle>filteredVehicles=vehicleRepository.findBySearchCriteria(criteria);
        return VehicleResponse.builder().vehicles(filteredVehicles).total(filteredVehicles.size()).page(1).build();
    }

    @Override
    public VehicleResponse getByModel(String model) {
        List<Vehicle>filteredVehicles= vehicleRepository.findByModel(model);
        return VehicleResponse.builder().vehicles(filteredVehicles).total(filteredVehicles.size()).page(1).build();
    }

    @Override
    public VehicleResponse searchByQuery(String query) {
        List<Vehicle>filteredVehicles= vehicleRepository.searchByQuery(query);
        return VehicleResponse.builder().vehicles(filteredVehicles).total(filteredVehicles.size()).page(1).build();
    }


}
