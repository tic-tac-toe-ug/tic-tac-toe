package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.UserRepository;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.controllers.dto.UserDetails;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.utils.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

  private static final Long INITIAL_SCORE = 0L;

  private final UserRepository userRepository;

  public User registerUser(RegistrationFrom registrationFrom) {
    if (isUserRegistered(registrationFrom)) throw new UserAlreadyExistsException();

    return saveUser(registrationFrom);
  }

  public Optional<UserDetails> getUserDetails(Long id) {
    return userRepository.findById(id).map(UserDetails::of);
  }

  private boolean isUserRegistered(RegistrationFrom registrationFrom) {
    return userRepository.existsByLoginOrEmail(registrationFrom.getLogin(), registrationFrom.getEmail());
  }

  //TODO: Add password encoding.
  private User saveUser(RegistrationFrom registrationFrom) {
    long numberOfUsers = userRepository.count();

    User build = User.builder()
      .login(registrationFrom.getLogin())
      .email(registrationFrom.getEmail())
      .password(registrationFrom.getPassword())
      .score(INITIAL_SCORE)
      .rank(++numberOfUsers)
      .build();

    return userRepository.save(build);
  }
}
