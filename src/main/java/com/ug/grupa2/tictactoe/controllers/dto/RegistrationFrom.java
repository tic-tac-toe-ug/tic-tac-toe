package com.ug.grupa2.tictactoe.controllers.dto;


import com.ug.grupa2.tictactoe.utils.validators.PasswordValueMatch;
import com.ug.grupa2.tictactoe.utils.validators.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@PasswordValueMatch.List({
  @PasswordValueMatch(
    field = "password",
    fieldMatch = "confirmPassword",
    message = "Passwords do not match!"
  )
})
public class RegistrationFrom {

  @NotNull
  @NotBlank(message = "Login can not be empty")
  @Size(min = 3, max = 15)
  private String login;

  @NotNull
  @NotBlank
  @ValidPassword
  private String password;

  @NotNull
  @NotBlank
  @ValidPassword
  private String confirmPassword;

  @NotNull
  @NotBlank
  @Email
  private String email;


  RegistrationFrom(String login, String password, String confirmPassword, String email) {
    this.login = login;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.email = email;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegistrationFrom that = (RegistrationFrom) o;
    return Objects.equals(login, that.login) && Objects.equals(password, that.password) && Objects.equals(confirmPassword, that.confirmPassword) && Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login, password, confirmPassword, email);
  }
}
