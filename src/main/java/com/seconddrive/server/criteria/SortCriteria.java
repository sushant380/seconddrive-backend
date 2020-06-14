package com.seconddrive.server.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SortCriteria {
    public enum Direction{
        DESC,
        ASC
    }
    private Direction direction;
    private String field;
}
