package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.controllers.dto.UserRankingStats;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @PostMapping
  public ResponseEntity<User> registerUser(@Valid @RequestBody RegistrationFrom registrationFrom) {
    //TODO:Move password encoder to better place
    User user = userService.registerUser(registrationFrom.getFormWithEncodedPassword(passwordEncoder));

    return ResponseEntity.ok(user);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserRankingStats> getUser(@PathVariable Long id) {
    return ResponseEntity.of(userService.getUserRankingStats(id));
  }
}
