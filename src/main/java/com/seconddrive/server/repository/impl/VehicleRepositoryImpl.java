package com.seconddrive.server.repository.impl;

import com.seconddrive.server.criteria.RangeCriteria;
import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.domain.Warehouse;
import com.seconddrive.server.repository.VehicleRepository;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.bind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

@Repository
public class VehicleRepositoryImpl implements VehicleRepository {
  @Inject MongoTemplate mongoTemplate;

  @Override
  public Vehicle findById(BigDecimal id) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(getMatchOperation("cars.vehicles._id", Operation.EQ, id));
    operations.add(getVehicleProjection());
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getUniqueMappedResult();
  }

  @Override
  public List<Vehicle> findAll() {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(getVehicleProjection());
    operations.add(sort(Sort.Direction.DESC, "date_added"));
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
  }

  @Override
  public List<Vehicle> findByWarehouse(BigDecimal id) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(getMatchOperation("_id", Operation.EQ, id));
    operations.add(getVehicleProjection());
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
  }

  @Override
  public List<Vehicle> findByModel(String model) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(getMatchOperation("cars.vehicles.model", Operation.EQ, model));
    operations.add(getVehicleProjection());
    operations.add(sort(Sort.Direction.DESC, "date_added"));
    TypedAggregation<Warehouse> aggregation =
            Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
  }

  @Override
  public List<Vehicle> findByYear(BigDecimal year) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(getMatchOperation("cars.vehicles.year_model", Operation.EQ, year));
    operations.add(getVehicleProjection());
    operations.add(sort(Sort.Direction.DESC, "date_added"));
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
  }

  @Override
  public List<Vehicle> findBySearchCriteria(SearchCriteria criteria) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    if (criteria.getMakes() != null && !criteria.getMakes().isEmpty()) {
      operations.add(getMatchOperation("cars.vehicles.make", Operation.IN, criteria.getMakes()));
    }
    if (criteria.getModels() != null && !criteria.getModels().isEmpty()) {
      operations.add(getMatchOperation("cars.vehicles.model", Operation.IN, criteria.getModels()));
    }
    if (criteria.getWarehouses() != null && !criteria.getWarehouses().isEmpty()) {
      operations.add(
          getMatchOperation(
              "_id",
              Operation.IN,
              criteria.getWarehouses().stream()
                  .mapToInt(BigDecimal::intValue)
                  .boxed()
                  .collect(Collectors.toList())));
    }
    if (criteria.getPriceRange() != null) {
      operations.add(
          getMatchOperation("cars.vehicles.price", Operation.BTW, criteria.getPriceRange()));
    }
    if (criteria.getYearRange() != null) {
      operations.add(
          getMatchOperation("cars.vehicles.year_model", Operation.BTW, criteria.getYearRange()));
    }

    operations.add(getVehicleProjection());
    if (criteria.getSort() != null) {
      operations.add(
          sort(
              Sort.Direction.valueOf(criteria.getSort().getDirection().toString()),
              criteria.getSort().getField()));
    }
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    List<Vehicle>filterVehicles=mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
    if (criteria.getDateRange() != null) {
        Date from= (Date)criteria.getDateRange().getMin();
        Date to=(Date)criteria.getDateRange().getMax();
        return filterVehicles.stream().filter(vehicle -> vehicle.getDateAdded().after(from) && vehicle.getDateAdded().before(to)).collect(Collectors.toList());
    }
    return filterVehicles;

  }

  private List<AggregationOperation> getVehicleAggregations() {
    return Arrays.asList(Aggregation.unwind("cars"), Aggregation.unwind("cars.vehicles"));
  }

  private ProjectionOperation getVehicleProjection() {
    return project()
        .andInclude(bind("_id", "cars.vehicles.id"))
        .andInclude(bind("make", "cars.vehicles.make"))
        .andInclude(bind("model", "cars.vehicles.model"))
        .andInclude(bind("year_model", "cars.vehicles.year"))
        .andInclude(bind("price", "cars.vehicles.price"))
        .andInclude(bind("licensed", "cars.vehicles.licensed"))
        .andInclude(bind("date_added", "cars.vehicles.dateAdded"));
  }

  private MatchOperation getMatchOperation(String field, Operation operation, Object... value) {
    switch (operation) {
      case EQ:
        return match(Criteria.where(field).is(value[0]));
      case IN:
        return match(Criteria.where(field).in((List) value[0]));
      case GT:
        return match(Criteria.where(field).gt(value[0]));
      case LT:
        return match(Criteria.where(field).lt(value[0]));
      case NOT:
        return match(Criteria.where(field).ne(value[0]));
      case GTE:
        return match(Criteria.where(field).gte(value[0]));
      case LTE:
        return match(Criteria.where(field).lte(value[0]));
      case BTW:
        RangeCriteria range = (RangeCriteria) value[0];

          return match(
              Criteria.where(field)
                  .gte(((BigDecimal) range.getMin()).doubleValue())
                  .lte(((BigDecimal) range.getMax()).doubleValue()));

      default:
        return null;
    }
  }

  public enum Operation {
    EQ,
    IN,
    GT,
    LT,
    NOT,
    GTE,
    LTE,
    BTW
  }
}
