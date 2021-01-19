package com.ug.grupa2.tictactoe;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class TicTacToeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }
}
