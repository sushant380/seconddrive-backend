package com.seconddrive.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Location {
    @Field("lat")
    private BigDecimal latitude;
    @Field("long")
    private BigDecimal longitude;
}
