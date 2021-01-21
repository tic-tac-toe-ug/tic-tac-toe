package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.controllers.dto.Ranking;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.controllers.dto.UserDetails;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable String username) {
    return ResponseEntity.of(userService.loadUserByUsernameWithoutPassword(username));
  }

  @GetMapping("/ranking")
  public ResponseEntity<Ranking> getUsersRanking() {
    return ResponseEntity.ok(userService.getUsersRanking());
  }
}
