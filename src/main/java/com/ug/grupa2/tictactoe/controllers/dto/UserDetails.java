package com.ug.grupa2.tictactoe.controllers.dto;

import com.ug.grupa2.tictactoe.entities.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(access = AccessLevel.PACKAGE)
public class UserDetails {

  @NotNull
  @NotBlank(message = "Login can not be empty")
  @Size(min = 3, max = 15)
  private final String login;

  private final Long score;

  private final Long rank;

  public static UserDetails of(User user) {
    return builder().login(user.getLogin())
      .score(user.getScore())
      .rank(user.getRank())
      .build();
  }

  public UserDetails updateRank(UserDetails user, Long newRankPosition){
    return builder().login(user.getLogin())
      .score(user.getScore())
      .rank(newRankPosition)
      .build();
  }
}
