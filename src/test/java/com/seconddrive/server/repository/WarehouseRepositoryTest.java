package com.seconddrive.server.repository;

import com.seconddrive.server.domain.Car;
import com.seconddrive.server.domain.Location;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.domain.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@SpringBootTest
public class WarehouseRepositoryTest {
    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    MongoTemplate mongoTemplate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @AfterEach
    public void cleanUp() {
        mongoTemplate.dropCollection("warehouses");
    }

    @Test
    void testSaveWareHouse() throws ParseException {
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
        warehouseRepository.save(demoWarehouse);
    }
}
