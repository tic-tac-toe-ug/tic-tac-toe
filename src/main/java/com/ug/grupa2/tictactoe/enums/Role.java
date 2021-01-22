package com.ug.grupa2.tictactoe.enums;

public enum Role {
  USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

  private final String name;

  Role(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
