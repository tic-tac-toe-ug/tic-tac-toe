package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.UserRepository;
import com.ug.grupa2.tictactoe.controllers.dto.Ranking;
import com.ug.grupa2.tictactoe.controllers.dto.RegistrationFrom;
import com.ug.grupa2.tictactoe.controllers.dto.UserDetails;
import com.ug.grupa2.tictactoe.entities.User;
import com.ug.grupa2.tictactoe.utils.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private static final long ID = 1L;
  private static final String LOGIN = "login";
  private static final String PASSWORD = "password";
  private static final String REPEATED_PASSWORD = "password";
  private static final String EMAIL = "email@edu.pl";
  private static final long INITIAL_RANK = 1L;
  private static final long INITIAL_SCORE = 0L;

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  private RegistrationFrom registrationFrom;

  private User user;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository);
    registrationFrom = getRegistrationFrom();
    user = getUser();
  }

  @Test
  public void registerUserShouldCreateUserWhenUserDoesNotExists() {
    //GIVEN
    when(userRepository.existsByLoginOrEmail(anyString(), anyString())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(user);

    //WHEN
    User result = userService.registerUser(registrationFrom);

    assertEquals(LOGIN, result.getLogin());
    assertEquals(EMAIL, result.getEmail());
    assertEquals(PASSWORD, result.getPassword());
    assertEquals(INITIAL_SCORE, result.getScore());
    assertEquals(INITIAL_RANK, result.getRank());
  }

  @Test
  public void registerUserShouldThrowExceptionWhenUserExists() {
    //GIVEN
    when(userRepository.existsByLoginOrEmail(anyString(), anyString())).thenReturn(true);

    //THEN
    assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registrationFrom));
  }

  @Test
  public void getUserDetailsReturnsDetailsWhenUserExists() {
    //GIVEN
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    //WHEN
    Optional<UserDetails> result = userService.getUserDetails(ID);

    //THEN
    assertTrue(result.isPresent());
  }

  @Test
  public void getUserDetailsReturnsEmptyWhenUserDoesNotExist() {
    //GIVEN
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    //WHEN
    Optional<UserDetails> result = userService.getUserDetails(ID);

    //THEN
    assertFalse(result.isPresent());
  }

  @Test
  public void getUsersRankingShouldReturnEmptyListWhenEmptyRepository() {
    //GIVEN
    when(userRepository.findByOrderByScoreDesc()).thenReturn(Collections.emptyList());

    //WHEN
    Ranking usersRanking = userService.getUsersRanking();

    //THEN
    assertEquals(0, usersRanking.getRanking().size());
  }

  @Test
  public void getUsersRankingShouldReturnNonEMptyListWhenUserExists() {
    //GIVEN
    User user = getUser();
    when(userRepository.findByOrderByScoreDesc()).thenReturn(Collections.singletonList(user));

    //WHEN
    Ranking usersRanking = userService.getUsersRanking();

    //THEN
    assertEquals(1, usersRanking.getRanking().size());
  }

  private User getUser() {
    return User.builder()
      .id(ID)
      .login(LOGIN)
      .email(EMAIL)
      .password(PASSWORD)
      .rank(INITIAL_RANK)
      .score(INITIAL_SCORE).build();
  }

  private RegistrationFrom getRegistrationFrom() {
    return new RegistrationFrom(LOGIN, PASSWORD, REPEATED_PASSWORD, EMAIL);
  }
}
