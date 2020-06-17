package com.seconddrive.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seconddrive.server.criteria.RangeCriteria;
import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.criteria.SortCriteria;
import com.seconddrive.server.domain.Car;
import com.seconddrive.server.domain.Location;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.domain.Warehouse;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VehicleRepositoryTest {
  @Inject VehicleRepository vehicleRepository;

  @Inject MongoTemplate mongoTemplate;

  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @AfterEach
  public void cleanUp() {
    mongoTemplate.dropCollection("warehouses");
  }

  @BeforeEach
  void setup() throws Exception {
    Warehouse demoWarehouse =
        Warehouse.builder()
            .id(BigDecimal.ONE)
            .location(
                Location.builder()
                    .latitude(BigDecimal.valueOf(17.59))
                    .longitude(BigDecimal.valueOf(15.67))
                    .build())
            .cars(
                Car.builder()
                    .location("West wing")
                    .vehicles(
                        Arrays.asList(
                            Vehicle.builder()
                                .id(BigDecimal.ONE)
                                .dateAdded(dateFormat.parse("2019-10-12"))
                                .licensed(true)
                                .make("Hyundai")
                                .model("Accent")
                                .price(BigDecimal.valueOf(4234.0))
                                .year(BigDecimal.valueOf(2016))
                                .build(),
                            Vehicle.builder()
                                .id(BigDecimal.TEN)
                                .dateAdded(dateFormat.parse("2019-09-12"))
                                .licensed(true)
                                .make("BMW")
                                .model("Q3")
                                .price(BigDecimal.valueOf(8234.0))
                                .year(BigDecimal.valueOf(2017))
                                .build()))
                    .build())
            .build();
    Warehouse emptyWarehouse =
        Warehouse.builder()
            .id(BigDecimal.valueOf(3))
            .location(
                Location.builder()
                    .latitude(BigDecimal.valueOf(17.69))
                    .longitude(BigDecimal.valueOf(15.87))
                    .build())
            .build();
    ObjectMapper mapper = new ObjectMapper();
    String data = mapper.writeValueAsString(demoWarehouse);
    data = data.replaceAll("year", "year_model");
    Document doc = Document.parse(data);
    mongoTemplate.insert(doc, "warehouses");
    doc = Document.parse(mapper.writeValueAsString(emptyWarehouse));
    mongoTemplate.insert(doc, "warehouses");
  }

  @Test
  public void findById() {
    Vehicle vehicle = vehicleRepository.findById(BigDecimal.ONE);
    assertThat(vehicle.getId()).isEqualTo(BigDecimal.ONE);
  }

  @Test
  public void findAll() {
    List<Vehicle> allVehicles = vehicleRepository.findAll();
    System.out.println(allVehicles);
    assertThat(allVehicles).size().isGreaterThan(0);
    assertThat(allVehicles)
        .filteredOn(vehicle -> vehicle.getMake().equals("BMW"))
        .extracting(Vehicle::getModel)
        .contains("Q3");
  }

  @Test
  public void findByWarehouse() {
    List<Vehicle> vehicles = vehicleRepository.findByWarehouse(BigDecimal.valueOf(3));
    assertThat(vehicles).isEmpty();
  }

  @Test
  public void findByMake() {
    assertThat(vehicleRepository.findByMake("Hyundai"))
        .extracting(Vehicle::getId).first()
        .isEqualTo(BigDecimal.valueOf(1));
  }

  @Test
  public void findByYear() {
    assertThat(vehicleRepository.findByYear(BigDecimal.valueOf(2016)))
        .extracting(Vehicle::getPrice)
        .first()
        .isEqualTo(BigDecimal.valueOf(4234.0));
  }

  @Test
  public void findBySearchCriteria() {
    SearchCriteria criteria = new SearchCriteria();
    criteria.setWarehouses(Arrays.asList(BigDecimal.valueOf(1)));
    criteria.setMakes(Arrays.asList("BMW"));
    SortCriteria sortCriteria=new SortCriteria();
    sortCriteria.setField("make");
    sortCriteria.setDirection(SortCriteria.Direction.ASC);
    criteria.setSort(sortCriteria);
    assertThat(vehicleRepository.findBySearchCriteria(criteria))
        .extracting(Vehicle::getLicensed)
        .contains(true);
    assertThat(vehicleRepository.findBySearchCriteria(criteria))
        .extracting(Vehicle::getMake)
        .doesNotContain("Hyundai");
  }

  @Test
  public void testRange_findBySearchCriteria() throws ParseException {
    SearchCriteria criteria = new SearchCriteria();
    RangeCriteria priceRange = new RangeCriteria();
    priceRange.setMax(BigDecimal.valueOf(10000));
    priceRange.setMin(BigDecimal.valueOf(6000));
    criteria.setPriceRange(priceRange);
    List<Vehicle> vehicles = vehicleRepository.findBySearchCriteria(criteria);
    assertThat(vehicles).extracting(Vehicle::getId).first().isEqualTo(BigDecimal.valueOf(10));
  }

}
