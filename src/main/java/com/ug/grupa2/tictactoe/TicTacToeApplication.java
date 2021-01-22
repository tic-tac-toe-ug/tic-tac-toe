package com.ug.grupa2.tictactoe;

import com.ug.grupa2.tictactoe.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TicTacToeApplication implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    final String username = "admin";
    if (!this.userRepository.findByUsername(username).isPresent()) {
      final String adminPass = "S3cr3tAdmin!";
      this.userRepository.save(
        User.builder()
          .username(username)
          .email("admin@admin.com")
          .password(encoder.encode(adminPass))
          .roles("ROLE_USER,ROLE_ADMIN")
          .score(0L)
          .rank(this.userRepository.count() + 1)
          .build());
    }
  }
}
