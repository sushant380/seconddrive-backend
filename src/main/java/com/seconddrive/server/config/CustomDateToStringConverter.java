package com.seconddrive.server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
@ReadingConverter
public class CustomDateToStringConverter implements Converter<Date,String> {
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String convert(Date date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(date+" : "+dateFormat.format(date));
        return dateFormat.format(date);
    }
}
