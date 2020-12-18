package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> registerUser(@Valid @RequestBody RegistrationFrom registrationFrom) {
    User user = userService.registerUser(registrationFrom);

    return ResponseEntity.ok(user);
  }

  //TODO: Add DTO class without password.
  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    return ResponseEntity.of(userService.getUser(id));
  }
}
