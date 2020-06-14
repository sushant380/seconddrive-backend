package com.seconddrive.server.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class SearchCriteria {
    private List<BigDecimal> warehouses;
    private List<String> models;
    private List<String>makes;
    private RangeCriteria priceRange;
    private RangeCriteria yearRange;
    private RangeCriteria dateRange;
   private SortCriteria sort;
}
