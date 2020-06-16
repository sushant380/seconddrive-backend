package com.seconddrive.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Vehicle {
    @Field("_id")
    @JsonProperty("_id")
    private BigDecimal id;
    private String make;
    private String model;
    @Field("year_model")
    private BigDecimal year;
    private BigDecimal price;
    private Boolean licensed;
    @Field("date_added")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateAdded;
    private String warehouse;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
