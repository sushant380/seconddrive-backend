package com.seconddrive.server.dto;

import com.seconddrive.server.domain.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VehicleResponse {
    private List<Vehicle> vehicles;
    private Integer total;
    private Integer page;
}
