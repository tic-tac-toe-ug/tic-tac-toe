package com.ug.grupa2.tictactoe.controllers.dto;

import com.ug.grupa2.tictactoe.entities.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Rank {
  private final List<UserDetails> ranking;

  public static Rank from(List<User> users) {
    List<UserDetails> ranking = users.stream()
      .map(UserDetails::of)
      .collect(Collectors.toList());

    List<UserDetails> defensiveCopy = Collections.unmodifiableList(new ArrayList<>(ranking));

    return new Rank(defensiveCopy);
  }
}
