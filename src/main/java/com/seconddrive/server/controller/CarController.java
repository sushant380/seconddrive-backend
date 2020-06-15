package com.seconddrive.server.controller;

import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.dto.VehicleResponse;
import com.seconddrive.server.repository.VehicleRepository;
import com.seconddrive.server.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@CrossOrigin("http://localhost:3100")
@RestController
@RequestMapping("/cars")
public class CarController {

    @Inject
    VehicleService vehicleService;

    @GetMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<VehicleResponse> getCars(){
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Vehicle> getCarById(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(vehicleService.getVehicleById(id), HttpStatus.OK);
    }

    @PostMapping(path="/search",consumes = "application/json", produces = "application/json")
    public ResponseEntity<VehicleResponse> searchCars(@RequestBody SearchCriteria searchCriteria){
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

    @GetMapping(path = "/model/{model}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<VehicleResponse> getCarById(@PathVariable(value = "model") String model){
        return new ResponseEntity<>(vehicleService.getByModel(model), HttpStatus.OK);
    }

    @GetMapping(path = "/search", consumes = "application/json", produces = "application/json")
    public ResponseEntity<VehicleResponse> searchByQuery(@RequestParam(value = "q") String q){
        return new ResponseEntity<>(vehicleService.searchByQuery(q), HttpStatus.OK);
    }

}
