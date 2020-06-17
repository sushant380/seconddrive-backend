package com.seconddrive.server.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.seconddrive.server.converter.BigDecimalToDecimalConverter;
import com.seconddrive.server.converter.CustomDateToStringConverter;
import com.seconddrive.server.converter.CustomStringToDateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom mongo db configuration to include custom converters for dates and decimals.
 */
@Configuration
public class MongoDbConfiguration extends AbstractMongoClientConfiguration {
  @Inject CustomDateToStringConverter customDateToStringConverter;
  @Inject CustomStringToDateConverter customStringToDateConverter;
  @Inject BigDecimalToDecimalConverter bigDecimalToDecimalConverter;
  @Value("${spring.data.mongodb.host}")
  private String host;
  @Value("${spring.data.mongodb.port}")
  private String port;
  @Value("${spring.data.mongodb.database}")
  private String database;

  @Override
  public MongoClient mongoClient() {
    return MongoClients.create("mongodb://" + host + ":" + port);
  }

  @Override
  protected String getDatabaseName() {
    return database;
  }

  protected void configureConverters(
      MongoCustomConversions.MongoConverterConfigurationAdapter converterConfigurationAdapter) {
    List list = new ArrayList<>();
    list.add(customDateToStringConverter);
    list.add(customStringToDateConverter);
    list.add(bigDecimalToDecimalConverter);
    converterConfigurationAdapter.registerConverters(list);
  }
}
