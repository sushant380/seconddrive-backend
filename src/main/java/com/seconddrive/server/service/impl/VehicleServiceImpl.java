package com.seconddrive.server.service.impl;

import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;
import com.seconddrive.server.repository.VehicleRepository;
import com.seconddrive.server.service.VehicleService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vehicle data service implementation
 */
@Service
public class VehicleServiceImpl implements VehicleService {
  @Inject VehicleRepository vehicleRepository;

  /**
   * Get vehicle response with all vehicles available
   * @return Vehicle response with count
   */
  @Override
  public VehicleResponse getAllVehicles() {
    List<Vehicle> allVehicles = vehicleRepository.findAll();
    return VehicleResponse.builder()
        .vehicles(allVehicles)
        .total(allVehicles.size())
        .page(1)
        .build();
  }

  /**
   * Get vehicle by vehicle ID
   * @param id Vehicle Id
   * @return Vehicle details
   */
  @Override
  public Vehicle getVehicleById(Long id) {
    return vehicleRepository.findById(BigDecimal.valueOf(id));
  }

  /**
   * Search vehicles based on model, makes, warehouses, year, prices
   * @param criteria search criteria
   * @return  Vehicle response with total
   */
  @Override
  public VehicleResponse searchVehicle(SearchCriteria criteria) {
    List<Vehicle> filteredVehicles = vehicleRepository.findBySearchCriteria(criteria);
    List<Vehicle> finalList=filteredVehicles;
    if (criteria.getDateRange() != null) {
      Date from = (Date) criteria.getDateRange().getMin();
      Date to = (Date) criteria.getDateRange().getMax();
      finalList= filteredVehicles.stream()
              .filter(
                      vehicle -> vehicle.getDateAdded().after(from) && vehicle.getDateAdded().before(to))
              .collect(Collectors.toList());
    }
    return VehicleResponse.builder()
        .vehicles(finalList)
        .total(finalList.size())
        .page(1)
        .build();
  }

  /**
   * Get vehicles based on model
   * @param make Vehicle model
   * @return list of vehicles
   */
  @Override
  public VehicleResponse getByMake(String make) {
    List<Vehicle> filteredVehicles = vehicleRepository.findByMake(make);
    return VehicleResponse.builder()
        .vehicles(filteredVehicles)
        .total(filteredVehicles.size())
        .page(1)
        .build();
  }

  /**
   * Get vehicle based on query for model and make
   * @param query query
   * @return List of vehicles
   */
  @Override
  public VehicleResponse searchByQuery(String query) {
    List<Vehicle> filteredVehicles = vehicleRepository.searchByQuery(query);
    return VehicleResponse.builder()
        .vehicles(filteredVehicles)
        .total(filteredVehicles.size())
        .page(1)
        .build();
  }
}
