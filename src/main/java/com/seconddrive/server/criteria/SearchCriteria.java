package com.seconddrive.server.criteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Search criteria
 */
@NoArgsConstructor
@Data
@ApiModel(value = "Search criteria")
public class SearchCriteria {
  @ApiModelProperty(
          name = "warehouses",
          value = "list of warehouses ids",
          required = false,
          example = "[1, 2]",
          dataType = "array")
  private List<BigDecimal> warehouses;
  @ApiModelProperty(
          name = "models",
          value = "list of models",
          required = false,
          example = "['Jetta 2', 'RX']",
          dataType = "array")
  private List<String> models;
  @ApiModelProperty(
          name = "makes",
          value = "list of makers",
          required = false,
          example = "['BMW', 'FIAT']",
          dataType = "array")
  private List<String> makes;
  @ApiModelProperty(
          name = "pricerange",
          value = "Price range",
          required = false,
          example = "{min:10,max:100}",
          dataType = "array")
  private RangeCriteria priceRange;
  @ApiModelProperty(
          name = "yearrange",
          value = "Year range",
          required = false,
          example = "{min:2009,max:2010}",
          dataType = "array")
  private RangeCriteria yearRange;
  @ApiModelProperty(
          name = "daterange",
          value = "Date range",
          required = false,
          example = "{min:'2009-09-10'',max:'2010-09-10'}",
          dataType = "array")
  private RangeCriteria dateRange;
  @ApiModelProperty(
          name = "sort",
          value = "Sort info",
          required = false,
          example = "{direction: 'asc',field:'col'}",
          dataType = "array")
  private SortCriteria sort;
}
