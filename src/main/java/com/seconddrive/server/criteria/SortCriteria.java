package com.seconddrive.server.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sort criteria to sort result asc or desc.
 */
@NoArgsConstructor
@Data
public class SortCriteria {
  private Direction direction;
  private String field;
  public enum Direction {
    DESC,
    ASC
  }
}
