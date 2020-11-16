package com.ug.grupa2.tictactoe;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Controller
@AllArgsConstructor
public class TicTacToeApplication implements CommandLineRunner {

  private final DataEntityRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }

  @GetMapping("/resource")
  @ResponseBody
  public Map<String, Object> home() {
    DataEntity dataEntity = this.repository.findAll().stream()
      .findAny()
      .orElse(new DataEntity(-1, "Nothing in db :("));

    Map<String, Object> model = new HashMap<>();
    model.put("id", dataEntity.getId().toString());
    model.put("content", dataEntity.getContent());
    return model;
  }


  @Override
  public void run(String... args) throws Exception {
    if (this.repository.count() == 0) {
      this.repository.save(new DataEntity(1, "Hello from db"));
    }
  }
}
