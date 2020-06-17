package com.seconddrive.server.repository.impl;

import com.seconddrive.server.criteria.RangeCriteria;
import com.seconddrive.server.criteria.SearchCriteria;
import com.seconddrive.server.domain.Vehicle;
import com.seconddrive.server.domain.Warehouse;
import com.seconddrive.server.repository.VehicleRepository;
import org.bson.BsonRegularExpression;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.bind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

/** Vehicle Repository implementation */
@Repository
public class VehicleRepositoryImpl implements VehicleRepository {
  @Inject MongoTemplate mongoTemplate;

  /**
   * Find vehicle by vehicle ID
   *
   * @param id vehicle id
   * @return Vehicle
   */
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

  /**
   * Find all vehicles in db
   *
   * @return List<Vehicle>
   */
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

  /**
   * Find vehicles by warehouse id
   *
   * @param id warehouse id
   * @return List<Vehicle>
   */
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

  /**
   * Find vehicles by make type
   *
   * @param make make type
   * @return List<Vehicle>
   */
  @Override
  public List<Vehicle> findByMake(String make) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(getMatchOperation("cars.vehicles.make", Operation.EQ, make));
    operations.add(getVehicleProjection());
    operations.add(sort(Sort.Direction.DESC, "date_added"));
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
  }

  /**
   * Find vehicles by year made
   *
   * @param year year
   * @return List<Vehicle>
   */
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

  /**
   * Find vehicles by search criteria
   *
   * @param criteria criteria
   * @return List<Vehicle>
   */
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
    List<Vehicle> filterVehicles =
        mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
    return filterVehicles;
  }

  /**
   * Search all vehicles whose model or make matches with query
   * @param q query to match
   * @return List<Vehicle>
   */
  @Override
  public List<Vehicle> searchByQuery(String q) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.addAll(getVehicleAggregations());
    operations.add(
        Aggregation.addFields()
            .addFieldWithValue(
                "yearString", ConvertOperators.ToString.toString("cars.vehicles.year"))
            .build());

    Query query = new Query();
    query.addCriteria(Criteria.where("cars.vehicles.model").regex(".*" + q + ".*"));
    Criteria criteria = new Criteria();

    Criteria modelCriteria = new Criteria("cars.vehicles.model");
    BsonRegularExpression modelRegex = new BsonRegularExpression(".*" + q + ".*");
    modelCriteria.regex(modelRegex);
    Criteria makeCriteria = new Criteria("cars.vehicles.make");
    BsonRegularExpression makeRegex = new BsonRegularExpression(".*" + q + ".*");
    makeCriteria.regex(makeRegex);

    Criteria yearCriteria = new Criteria("yearString");
    BsonRegularExpression yearRegex = new BsonRegularExpression(".*" + q + ".*");
    yearCriteria.regex(yearRegex);
    criteria.orOperator(modelCriteria, makeCriteria, yearCriteria);
    operations.add(match(criteria));
    operations.add(getVehicleProjection());
    operations.add(sort(Sort.Direction.DESC, "date_added"));
    TypedAggregation<Warehouse> aggregation =
        Aggregation.newAggregation(Warehouse.class, operations);
    return mongoTemplate.aggregate(aggregation, Vehicle.class).getMappedResults();
  }

  /**
   * Create common unwind aggregation for cars and vehicles
   * @return
   */
  private List<AggregationOperation> getVehicleAggregations() {
    return Arrays.asList(Aggregation.unwind("cars"), Aggregation.unwind("cars.vehicles"));
  }

  /**
   * Create projection of vehicle fields with result
   * @return
   */
  private ProjectionOperation getVehicleProjection() {
    return project()
        .andInclude(bind("_id", "cars.vehicles.id"))
        .andInclude(bind("make", "cars.vehicles.make"))
        .andInclude(bind("model", "cars.vehicles.model"))
        .andInclude(bind("year_model", "cars.vehicles.year"))
        .andInclude(bind("price", "cars.vehicles.price"))
        .andInclude(bind("licensed", "cars.vehicles.licensed"))
        .andInclude(bind("date_added", "cars.vehicles.dateAdded"))
        .andInclude(bind("warehouse", "name"))
        .andInclude(bind("location", "cars.location"))
        .andInclude(bind("latitude", "location.latitude"))
        .andInclude(bind("longitude", "location.longitude"));
  }

  /**
   * Creation match operation on field based on operation and value
   * @param field
   * @param operation
   * @param value
   * @return
   */
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
      case CONTAINS:
        return match(Criteria.where(field).regex(".*" + value[0] + ".*"));
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

  /**
   * Operation enums.
   */
  public enum Operation {
    EQ,//Equals
    IN,//IN
    GT,//Greater than
    LT,// Less than
    NOT,// not equal to
    GTE,// Greater than or equal to
    LTE,// Less than or equal to
    BTW,// Between to values
    CONTAINS// contains thing
  }
}
