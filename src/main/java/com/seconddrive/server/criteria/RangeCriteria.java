package com.seconddrive.server.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class RangeCriteria {
    private Object min;
    private Object max;
}
