package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.controllers.dto.Ranking;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class UserController {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @PostMapping
  public ResponseEntity<User> registerUser(@Valid @RequestBody RegistrationFrom registrationFrom) {
    //TODO:Move password encoder to better place
    User user = userService.registerUser(registrationFrom.getFormWithEncodedPassword(passwordEncoder));

    return ResponseEntity.ok(user);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(
    @PathVariable("id") Long id,
    @RequestBody RegistrationFrom registrationFrom) {
    final User requester = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Received id: {}, user form: {}, requestingUser: {}", id, registrationFrom, requester);
    Optional<User> updatedUser = this.userService.updateUser(
      id,
      registrationFrom.getFormWithEncodedPassword(this.passwordEncoder),
      requester);
    return ResponseEntity.of(updatedUser.map(user -> user.withPassword("")));
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
