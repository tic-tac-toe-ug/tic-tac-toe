package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.UserRepository;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.controllers.dto.UserRankingStats;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.utils.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {

  private static final Long INITIAL_SCORE = 0L;

  private final UserRepository userRepository;

  public User registerUser(RegistrationFrom registrationFrom) {
    if (isUserRegistered(registrationFrom)) throw new UserAlreadyExistsException();

    return saveUser(registrationFrom);
  }

  public Optional<UserRankingStats> getUserRankingStats(Long id) {
    return userRepository.findById(id).map(UserRankingStats::of);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user != null) {
      return user;
    }
    throw new UsernameNotFoundException(
      "User '" + username + "' not found");
  }

  private boolean isUserRegistered(RegistrationFrom registrationFrom) {
    return userRepository.existsByUsernameOrEmail(registrationFrom.getLogin(), registrationFrom.getEmail());
  }
  //TODO: Add password encoding.

  private User saveUser(RegistrationFrom registrationFrom) {
    long numberOfUsers = userRepository.count();

    User build = User.builder()
      .username(registrationFrom.getLogin())
      .email(registrationFrom.getEmail())
      .password(registrationFrom.getPassword())
      .score(INITIAL_SCORE)
      .rank(++numberOfUsers)
      .build();

    return userRepository.save(build);
  }
}
