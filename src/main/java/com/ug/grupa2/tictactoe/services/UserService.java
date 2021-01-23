package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.UserRepository;
import com.ug.grupa2.tictactoe.controllers.dto.Ranking;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.controllers.dto.UserDetails;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.enums.MoveResult;
import com.ug.grupa2.tictactoe.utils.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class UserService implements UserDetailsService {

  private static final Long INITIAL_SCORE = 0L;
  public static final Long POINTS_FOR_WINNING_GAME = 3L;
  public static final Long POINTS_FOR_TIE = 1L;

  private final UserRepository userRepository;

  public User registerUser(RegistrationFrom registrationFrom) {
    if (isUserRegistered(registrationFrom)) throw new UserAlreadyExistsException();

    return saveUser(registrationFrom);
  }

  public Optional<UserDetails> getUserDetails(Long id) {
    return userRepository.findById(id).map(getUserDetailsWithUpdatedRank());
  }

  public Ranking getUsersRanking() {
    List<User> usersByScore = getUsersByScore();
    //TODO: Temporary :>
    long index = 1;
    for (User user : usersByScore) {
      user.setRank(index++);
    }

    return Ranking.from(usersByScore);
  }

  public Ranking resetRanking() {
    this.getUsersByScore()
      .stream().map(user -> user.withScore(0L))
      .forEach(this.userRepository::save);
    return this.getUsersRanking();
  }


  public Optional<User> loadUserByUsernameWithoutPassword(String username) {
    return userRepository.findByUsername(username)
      .map(user -> user.withPassword(""));
  }

  @Override
  public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
  }

  void updateUsersScore(MoveResult gameResult, String user1, String user2) {
    if (MoveResult.USER1_WON == gameResult) {
      updateUsersScore(user1, POINTS_FOR_WINNING_GAME);
    }
    if (MoveResult.USER2_WON == gameResult) {
      updateUsersScore(user2, POINTS_FOR_WINNING_GAME);
    }
    if (MoveResult.TIE == gameResult) {
      updateUsersScore(user1, POINTS_FOR_TIE);
      updateUsersScore(user2, POINTS_FOR_TIE);
    }
  }

  private void updateUsersScore(String user1, Long points) {
    userRepository.findByUsername(user1).ifPresent(u -> u.setScore(u.getScore() + points));
  }

  private boolean isUserRegistered(RegistrationFrom registrationFrom) {
    return userRepository.existsByUsernameOrEmail(registrationFrom.getLogin(), registrationFrom.getEmail());
  }

  private User saveUser(RegistrationFrom registrationFrom) {
    long numberOfUsers = userRepository.count();

    User build = User.builder()
      .username(registrationFrom.getLogin())
      .email(registrationFrom.getEmail())
      .password(registrationFrom.getPassword())
      .score(INITIAL_SCORE)
      .rank(++numberOfUsers)
      .roles("ROLE_USER")
      .build();

    return userRepository.save(build);
  }

  private List<User> getUsersByScore() {
    return Collections.unmodifiableList(new ArrayList<>(userRepository.findByOrderByScoreDesc()));
  }

  private int getCurrentRankPosition(User user) {
    return getUsersByScore().indexOf(user);
  }

  private Function<User, UserDetails> getUserDetailsWithUpdatedRank() {
    return user -> {
      int newRankPosition = getCurrentRankPosition(user);
      UserDetails details = UserDetails.of(user);

      return details.updateRank(details, (long) newRankPosition);
    };
  }

  public Optional<User> updateUser(Long idOfUserToBeUpdated, RegistrationFrom registrationFrom, User requester) {
    Optional<User> maybeUpdated = this.userRepository.findById(idOfUserToBeUpdated)
      .filter(canEditUser(requester, registrationFrom))
      .map(updateUserWithRegistrationForm(registrationFrom));
    maybeUpdated.ifPresent(this.userRepository::save);
    return maybeUpdated;
  }

  private Function<User, User> updateUserWithRegistrationForm(RegistrationFrom registrationFromWithEncodedPassword) {
    return existingUser -> {
      User updatedUser = existingUser;
      if (!Strings.isEmpty(registrationFromWithEncodedPassword.getEmail())) {
        updatedUser = updatedUser.withEmail(registrationFromWithEncodedPassword.getEmail());
      }
      if (!Strings.isEmpty(registrationFromWithEncodedPassword.getLogin())) {
        updatedUser = updatedUser.withUsername(registrationFromWithEncodedPassword.getLogin());
      }
      if (!Strings.isEmpty(registrationFromWithEncodedPassword.getPassword())) {
        updatedUser = updatedUser.withPassword(registrationFromWithEncodedPassword.getPassword());
      }
      return updatedUser;
    };
  }

  private Predicate<User> canEditUser(User requester, RegistrationFrom registrationFrom) {
    return user -> {
      // admin can change everything
      if (requester.getRoles().contains("ADMIN")) return true;
      // user can only change password
      return registrationFrom.getLogin().equals(user.getUsername())
        && registrationFrom.getEmail().equals(user.getEmail());
    };
  }

  public void deleteByUsername(String username) {
    this.userRepository.deleteByUsername(username);
  }
}
