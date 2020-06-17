package com.seconddrive.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Vehicle details
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "Car Details")
public class Vehicle {
  @ApiModelProperty(
          name = "id",
          value = "Vehicle id",
          required = false,
          example = "1",
          dataType = "number")
  @Field("_id")
  @JsonProperty("_id")
  private BigDecimal id;

  @ApiModelProperty(
          name = "make",
          value = "Car make",
          required = false,
          example = "BMW",
          dataType = "string")
  private String make;

  @ApiModelProperty(
          name = "warehouses",
          value = "list of warehouses ids",
          required = false,
          example = "[1, 2]",
          dataType = "array")
  private String model;

  @ApiModelProperty(
          name = "year",
          value = "Make year",
          required = false,
          example = "2009",
          dataType = "number")
  @Field("year_model")
  private BigDecimal year;

  @ApiModelProperty(
          name = "price",
          value = "Car price",
          required = false,
          example = "3939",
          dataType = "number")
  private BigDecimal price;

  @ApiModelProperty(
          name = "licensed",
          value = "Car licensed status",
          required = false,
          example = "true",
          dataType = "boolean")
  private Boolean licensed;

  @ApiModelProperty(
          name = "dateAdded",
          value = "Date for sale added",
          required = false,
          example = "2009-09-10",
          dataType = "date")
  @Field("date_added")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date dateAdded;

  @ApiModelProperty(
          name = "warehouse",
          value = "Warehouse of car",
          required = false,
          example = "w1",
          dataType = "string")
  private String warehouse;
  @ApiModelProperty(
          name = "location",
          value = "location of warehouse",
          required = false,
          example = "amsterdam",
          dataType = "string")
  private String location;
  @ApiModelProperty(
          name = "latitude",
          value = "warehouse latitude",
          required = false,
          example = "53.34",
          dataType = "number")
  private BigDecimal latitude;
  @ApiModelProperty(
          name = "longitude",
          value = "warehouse longitude",
          required = false,
          example = "5.0",
          dataType = "number")
  private BigDecimal longitude;
}
