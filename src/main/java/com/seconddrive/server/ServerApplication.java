package com.seconddrive.server;

import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.criteria.SortCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

}
