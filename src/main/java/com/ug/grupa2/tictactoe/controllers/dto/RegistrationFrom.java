package com.ug.grupa2.tictactoe.controllers.dto;

import com.ug.grupa2.tictactoe.utils.validators.PasswordValueMatch;
import com.ug.grupa2.tictactoe.utils.validators.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordValueMatch.List({
  @PasswordValueMatch(
    field = "password",
    fieldMatch = "confirmPassword",
    message = "Passwords do not match!"
  )
})
@Data
public class RegistrationFrom {

  @NotNull
  @NotBlank(message = "Login can not be empty")
  @Size(min = 3, max = 15)
  private final String login;

  @NotNull
  @NotBlank
  @ValidPassword
  private final String password;

  @NotNull
  @NotBlank
  @ValidPassword
  private final String confirmPassword;

  @NotNull
  @NotBlank
  @Email
  private final String email;
}
