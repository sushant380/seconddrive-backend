package com.seconddrive.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.criteria.SortCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;
import com.seconddrive.server.service.VehicleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarControllerTest {
  @MockBean VehicleService vehicleService;

  @Autowired private MockMvc mockMvc;

  ObjectMapper mapper = new ObjectMapper();

  VehicleResponse response;

  Vehicle vehicle;

  @BeforeEach
  void setup() {
    vehicle =
        Vehicle.builder()
            .id(BigDecimal.ONE)
            .make("BMW")
            .model("Q6")
            .licensed(true)
            .price(BigDecimal.valueOf(3242))
            .year(BigDecimal.valueOf(2009))
            .warehouse("ware1")
            .dateAdded(new Date())
            .location("moon")
            .build();
    response = VehicleResponse.builder().vehicles(Arrays.asList(vehicle)).total(1).build();
  }

  /**
   * Check happy scenario.
   *
   * @throws Exception
   */
  @Test
  public void getCars() throws Exception {

    when(vehicleService.getAllVehicles()).thenReturn(response);
    this.mockMvc
        .perform(
            get("/cars")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64Utils.encodeToString("test:test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            mvcResult -> {
              String json = mvcResult.getResponse().getContentAsString();
              VehicleResponse actualObject = mapper.readValue(json, VehicleResponse.class);
              Assertions.assertThat(response.getTotal()).isEqualTo(actualObject.getTotal());
            });
  }

  @Test
  void getCarById() throws Exception {
    when(vehicleService.getVehicleById(1L)).thenReturn(vehicle);
    this.mockMvc
        .perform(
            get("/cars/1")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64Utils.encodeToString("test:test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            mvcResult -> {
              String json = mvcResult.getResponse().getContentAsString();
              Vehicle actualObject = mapper.readValue(json, Vehicle.class);
              Assertions.assertThat(vehicle.getId()).isEqualTo(actualObject.getId());
            });
  }

  @Test
  void searchCars() throws Exception {
    SearchCriteria criteria = new SearchCriteria();
    criteria.setWarehouses(Arrays.asList(BigDecimal.valueOf(1)));
    criteria.setMakes(Arrays.asList("BMW"));
    SortCriteria sortCriteria=new SortCriteria();
    sortCriteria.setField("make");
    sortCriteria.setDirection(SortCriteria.Direction.DESC);

    when(vehicleService.getAllVehicles()).thenReturn(response);
    this.mockMvc
        .perform(post("/cars/search")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64Utils.encodeToString("test:test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(criteria)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            mvcResult -> {
              String json = mvcResult.getResponse().getContentAsString();
              VehicleResponse actualObject = mapper.readValue(json, VehicleResponse.class);
              Assertions.assertThat(actualObject.getVehicles())
                  .extracting(Vehicle::getMake)
                  .first()
                  .isEqualTo(vehicle.getMake());
            });
  }

  @Test
  void getCarByMake() throws Exception {
    when(vehicleService.getByMake("Q6")).thenReturn(response);
    this.mockMvc
        .perform(
            get("/cars/make/Q6")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64Utils.encodeToString("test:test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            mvcResult -> {
              String json = mvcResult.getResponse().getContentAsString();
              VehicleResponse actualObject = mapper.readValue(json, VehicleResponse.class);
              Assertions.assertThat(actualObject.getVehicles())
                  .extracting(Vehicle::getModel)
                  .first()
                  .isEqualTo(vehicle.getModel());
            });
  }

  @Test
  void searchByQuery() throws Exception {
    when(vehicleService.searchByQuery("B")).thenReturn(response);
    this.mockMvc
        .perform(
            get("/cars/search?q=B")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64Utils.encodeToString("test:test".getBytes()))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            mvcResult -> {
              String json = mvcResult.getResponse().getContentAsString();
              VehicleResponse actualObject = mapper.readValue(json, VehicleResponse.class);
              Assertions.assertThat(actualObject.getVehicles())
                  .extracting(Vehicle::getMake)
                  .first()
                  .isEqualTo(vehicle.getMake());
            });
  }

    @Test
    void authenticationException() throws Exception {
        when(vehicleService.searchByQuery("B")).thenReturn(response);
        this.mockMvc
                .perform(
                        get("/cars")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requestParamException() throws Exception {
        when(vehicleService.searchByQuery("B")).thenReturn(response);
        this.mockMvc
                .perform(
                        get("/cars/search?")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64Utils.encodeToString("test:test".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
