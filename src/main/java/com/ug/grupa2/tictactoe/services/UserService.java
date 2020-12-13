package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.UserRepository;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.utils.exceptions.UserAlreadyExistsException;
import com.ug.grupa2.tictactoe.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * @param registrationFrom
   * @return
   */
  public User registerUser(RegistrationFrom registrationFrom) {
    if (isUserRegistered(registrationFrom)) throw new UserAlreadyExistsException();

    return saveUser(registrationFrom);
  }

  /**
   * @param id
   * @return
   */
  public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  private boolean isUserRegistered(RegistrationFrom registrationFrom) {
    return userRepository.existsByLoginOrEmail(registrationFrom.getLogin(), registrationFrom.getEmail());
  }

  //TODO: Add password encoding.
  private User saveUser(RegistrationFrom registrationFrom) {
    return userRepository.save(User.builder()
      .login(registrationFrom.getLogin())
      .email(registrationFrom.getEmail())
      .password(registrationFrom.getPassword()).build());
  }
}
