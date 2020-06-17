package com.seconddrive.server.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Range Criteria to define min and max points.
 */
@NoArgsConstructor
@Data
public class RangeCriteria {
  private Object min;
  private Object max;
}
