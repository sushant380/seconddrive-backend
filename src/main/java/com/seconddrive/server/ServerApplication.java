package com.seconddrive.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.criteria.SortCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.repository.VehicleRepository;
import com.seconddrive.server.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class ServerApplication {
  static ObjectMapper mapper=new ObjectMapper();
  public static void main(String[] args) throws IOException {
    SpringApplication.run(ServerApplication.class, args);
  }


}
