package com.seconddrive.server.service;

import com.seconddrive.server.criteria.RangeCriteria;
import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;
import com.seconddrive.server.repository.VehicleRepository;
import com.seconddrive.server.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VehicleServiceTest {
    @InjectMocks
    VehicleService vehicleService=new VehicleServiceImpl();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    VehicleRepository vehicleRepository;

    @Test
    void getAllVehicles(){
        Mockito.when(vehicleRepository.findAll()).thenReturn(Arrays.asList(Vehicle.builder().id(BigDecimal.valueOf(6)).build()));
        VehicleResponse response=vehicleService.getAllVehicles();
        assertThat(response.getTotal()).isEqualTo(1);
        assertThat(response.getVehicles()).extracting(Vehicle::getId).first().isEqualTo(BigDecimal.valueOf(6));
    }

    @Test
    void getVehicleById(){
        Mockito.when(vehicleRepository.findById(BigDecimal.ONE)).thenReturn(Vehicle.builder().id(BigDecimal.valueOf(6)).build());
        assertThat(vehicleService.getVehicleById(1L)).extracting(Vehicle::getId).isEqualTo(BigDecimal.valueOf(6));
    }

    @Test
    void searchVehicle(){
        SearchCriteria criteria = new SearchCriteria();
        criteria.setWarehouses(Arrays.asList(BigDecimal.valueOf(1)));
        criteria.setMakes(Arrays.asList("BMW"));
        Mockito.when(vehicleRepository.findBySearchCriteria(criteria)).thenReturn(
                Arrays.asList(
                Vehicle.builder().id(BigDecimal.valueOf(6)).make("BMW").build()));
        assertThat(vehicleService.searchVehicle(criteria).getVehicles()).extracting(Vehicle::getMake).first().isEqualTo("BMW");
    }

    @Test
    void searchVehicleONDateRange() throws ParseException {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setWarehouses(Arrays.asList(BigDecimal.valueOf(1)));
        RangeCriteria range=new RangeCriteria();
        range.setMax(dateFormat.parse("2019-01-01"));
        range.setMin(dateFormat.parse("2018-01-01"));
        criteria.setDateRange(range);
        Mockito.when(vehicleRepository.findBySearchCriteria(criteria)).thenReturn(
                Arrays.asList(
                        Vehicle.builder().id(BigDecimal.valueOf(6)).dateAdded(dateFormat.parse("2018-10-01")).make("BMW").build()));
        assertThat(vehicleService.searchVehicle(criteria).getVehicles()).extracting(Vehicle::getId).first().isEqualTo(BigDecimal.valueOf(6));
    }

    @Test
    void getByModel(){
        Mockito.when(vehicleRepository.findByMake("BMW")).thenReturn(Arrays.asList(Vehicle.builder().id(BigDecimal.valueOf(6)).build()));
        assertThat(vehicleService.getByMake("BMW").getVehicles()).extracting(Vehicle::getId).first().isEqualTo(BigDecimal.valueOf(6));

    }

    @Test
    void searchByQuery(){
        Mockito.when(vehicleRepository.searchByQuery("BMW")).thenReturn(Arrays.asList(Vehicle.builder().id(BigDecimal.valueOf(6)).build()));
        assertThat(vehicleService.searchByQuery("BMW").getVehicles()).extracting(Vehicle::getId).first().isEqualTo(BigDecimal.valueOf(6));

    }
}
