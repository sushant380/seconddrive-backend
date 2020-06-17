package com.seconddrive.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {
  static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws IOException {
    SpringApplication.run(ServerApplication.class, args);
  }
}
