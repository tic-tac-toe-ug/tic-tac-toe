package com.ug.grupa2.tictactoe.security;

import com.ug.grupa2.tictactoe.UserRepository;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataBaseInit {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  void addDefaultAdmin() {
    //UNIQUE constraint nie chciał mi zadziałać na bazie :| Naprawie potem (Mam nadzieje :D). Przemek
    boolean exists = userRepository.existsByUsernameOrEmail("admin", "admin@tictactoe.com");

    if (!exists) {
      userRepository.save(User.builder()
        .email("admin@tictactoe.com")
        .username("admin")
        .password(passwordEncoder.encode("admin"))
        .role(Role.ADMIN)
        .build());
    }
  }
}
