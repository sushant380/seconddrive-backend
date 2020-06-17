package com.seconddrive.server.converter;

import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Custom converter to convert string to date format while retrieving from db
 */
@Component
@ReadingConverter
public class CustomStringToDateConverter implements Converter<String, Date> {
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @SneakyThrows
  @Override
  public Date convert(String s) {
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return dateFormat.parse(s);
  }
}
