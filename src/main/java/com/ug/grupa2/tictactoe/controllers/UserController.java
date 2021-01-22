package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.controllers.dto.Ranking;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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

  @GetMapping("/admin")
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @DeleteMapping("{id}/admin")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/ranking")
  public ResponseEntity<Ranking> getUsersRanking() {
    return ResponseEntity.ok(userService.getUsersRanking());
  }

  @PutMapping("/ranking/admin")
  public ResponseEntity<?> resetRanking() {
    userService.resetRanking();

    return ResponseEntity.ok().build();
  }
}
