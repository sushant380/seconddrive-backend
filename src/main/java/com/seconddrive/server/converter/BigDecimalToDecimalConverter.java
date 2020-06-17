package com.seconddrive.server.converter;

import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Custom converter to convert BigDecimal value to double to avoid additional conversion at mongodb
 */
@Component
@WritingConverter
public class BigDecimalToDecimalConverter implements Converter<BigDecimal, Double> {
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @SneakyThrows
  @Override
  public Double convert(BigDecimal s) {
    return s.doubleValue();
  }
}
