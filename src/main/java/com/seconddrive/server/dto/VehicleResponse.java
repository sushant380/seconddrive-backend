package com.seconddrive.server.dto;

import com.seconddrive.server.domain.Vehicle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Vehicle response dto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(description = "Vehicle response object")
public class VehicleResponse {
  @ApiModelProperty(
          name = "vehicles",
          value = "List of vehicles",
          required = false,
          example = "[{...}]",
          dataType = "array")
  private List<Vehicle> vehicles;
  @ApiModelProperty(
          name = "total",
          value = "Total vehicle counts",
          required = false,
          example = "10",
          dataType = "number")
  private Integer total;
  @ApiModelProperty(
          name = "page",
          value = "Current page",
          required = false,
          example = "1",
          dataType = "number")
  private Integer page;
}
