package com.seconddrive.server.controller;

import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;
import com.seconddrive.server.service.VehicleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(path = "/cars")
public class CarController {

  @Inject VehicleService vehicleService;

  @ApiOperation(nickname = "getCars", value = "Get all cars", response = VehicleResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "List of cars", response = VehicleResponse.class),
        @ApiResponse(
            code = 400,
            message =
                "The request body is either malformed or contains unsupported parameter values."),
        @ApiResponse(code = 403, message = "Not authorized to call this service."),
        @ApiResponse(
            code = 500,
            message = "An unexpected error occurred while processing the request."),
        @ApiResponse(code = 502, message = "Application returned an unexpected error."),
        @ApiResponse(code = 504, message = "Application did not respond in a timely fashion.")
      })
  @GetMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<VehicleResponse> getCars() {
    return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
  }

  @ApiOperation(nickname = "getCarById", value = "Get Car details", response = Vehicle.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Car Details", response = Vehicle.class),
        @ApiResponse(
            code = 400,
            message =
                "The request body is either malformed or contains unsupported parameter values."),
        @ApiResponse(code = 403, message = "Not authorized to call this service."),
        @ApiResponse(
            code = 500,
            message = "An unexpected error occurred while processing the request."),
        @ApiResponse(code = 502, message = "Application returned an unexpected error."),
        @ApiResponse(code = 504, message = "Application did not respond in a timely fashion.")
      })
  @GetMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Vehicle> getCarById(@PathVariable(value = "id") Long id) {
    return new ResponseEntity<>(vehicleService.getVehicleById(id), HttpStatus.OK);
  }

  @ApiOperation(
      nickname = "search",
      value = "Search cars based on criteria",
      response = VehicleResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Search Cars on criteria",
            response = VehicleResponse.class),
        @ApiResponse(
            code = 400,
            message =
                "The request body is either malformed or contains unsupported parameter values."),
        @ApiResponse(code = 403, message = "Not authorized to call this service."),
        @ApiResponse(
            code = 500,
            message = "An unexpected error occurred while processing the request."),
        @ApiResponse(code = 502, message = "Application returned an unexpected error."),
        @ApiResponse(code = 504, message = "Application did not respond in a timely fashion.")
      })
  @PostMapping(path = "/search", consumes = "application/json", produces = "application/json")
  public ResponseEntity<VehicleResponse> searchCars(@RequestBody SearchCriteria searchCriteria) {
    return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
  }

  @ApiOperation(
      nickname = "make",
      value = "Search cars based on manufacturer",
      response = VehicleResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Search cars based on make",
            response = VehicleResponse.class),
        @ApiResponse(
            code = 400,
            message =
                "The request body is either malformed or contains unsupported parameter values."),
        @ApiResponse(code = 403, message = "Not authorized to call this service."),
        @ApiResponse(
            code = 500,
            message = "An unexpected error occurred while processing the request."),
        @ApiResponse(code = 502, message = "Application returned an unexpected error."),
        @ApiResponse(code = 504, message = "Application did not respond in a timely fashion.")
      })
  @GetMapping(path = "/make/{make}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<VehicleResponse> getCarByMake(@PathVariable(value = "make") String make) {
    return new ResponseEntity<>(vehicleService.getByMake(make), HttpStatus.OK);
  }

  @ApiOperation(
      nickname = "query",
      value = "Search cars that match by query string",
      response = VehicleResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Search cars that match by query string",
            response = VehicleResponse.class),
        @ApiResponse(
            code = 400,
            message =
                "The request body is either malformed or contains unsupported parameter values."),
        @ApiResponse(code = 403, message = "Not authorized to call this service."),
        @ApiResponse(
            code = 500,
            message = "An unexpected error occurred while processing the request."),
        @ApiResponse(code = 502, message = "Application returned an unexpected error."),
        @ApiResponse(code = 504, message = "Application did not respond in a timely fashion.")
      })
  @GetMapping(path = "/search", consumes = "application/json", produces = "application/json")
  public ResponseEntity<VehicleResponse> searchByQuery(@RequestParam(value = "q") String q) {
    return new ResponseEntity<>(vehicleService.searchByQuery(q), HttpStatus.OK);
  }
}
