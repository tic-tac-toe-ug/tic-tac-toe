package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.GameEntityRepository;
import com.ug.grupa2.tictactoe.entities.Game;
import com.ug.grupa2.tictactoe.enums.GameStatus;
import com.ug.grupa2.tictactoe.enums.MoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

  private static final String USER1 = "user1";
  private static final String USER2 = "user2";
  private static final int[] MOVES = new int[9];
  private static final GameStatus GAME_STATUS = GameStatus.CREATED;

  private static final int USER1_MARK = 1;
  private static final int USER2_MARK = 2;

  @Mock
  private GameEntityRepository gameRepository;

  private GameService gameService;

  @BeforeEach
  void setUp() {
    gameService = new GameService(gameRepository);
  }

  @Test
  public void createGameShouldCreateGameWithProperUserName() {
    // WHEN
    Game result = gameService.createGame(USER1);

    // THEN
    assertEquals(USER1, result.getUser1());
  }

  @Test
  public void getGamesShouldReturnAllGames() {
    // GIVEN
    List<Game> games = Arrays.asList(getSampleGame(1L), getSampleGame(2L));
    when(gameRepository.findAll()).thenReturn(games);

    // WHEN
    List<Game> result = gameService.getGames();

    // THEN
    assertArrayEquals(games.toArray(), result.toArray());
  }

  @Test
  public void deleteGameShouldThrowExceptionWhenGameDoesNotExist() {
    // GIVEN
    final Long ID = 1L;
    when(gameRepository.findById(ID)).thenReturn(Optional.empty());

    // THEN
    assertThrows(ResponseStatusException.class, () -> gameService.deleteGame(ID));
  }

  @Test
  public void playGameReturnsUnauthorizedIfMoveUnauthorized() {
    // GIVEN
    Game game = getSampleGame(1L);

    // WHEN
    MoveResult result = gameService.playGame(game, "user3", 1);

    // THEN
    assertEquals(MoveResult.UNAUTHORIZED, result);
  }

  @Test
  public void playGameReturnsInvalidMoveIfNoOtherPersonToPlay() {
    // GIVEN
    Game game = getSampleGame(1L);
    game.setUser2(null);

    // WHEN
    MoveResult result = gameService.playGame(game, USER1, 1);

    // THEN
    assertEquals(MoveResult.INVALID_MOVE, result);
  }

  @Test
  public void playGameReturnsInvalidMoveIfGameIsNotInProgress() {
    // GIVEN
    Game game = getSampleGame(1L);
    game.setGameStatus(GameStatus.USER2_WON);

    // WHEN
    MoveResult result = gameService.playGame(game, USER1, 1);

    // THEN
    assertEquals(MoveResult.INVALID_MOVE, result);
  }

  @Test
  public void playGameReturnsInvalidMoveIfNotPlayersTurn() {
    // GIVEN
    Game game = getSampleGame(1L);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    game.setFirstToMove(USER1);

    // WHEN
    MoveResult result = gameService.playGame(game, USER2, 1);

    // THEN
    assertEquals(MoveResult.INVALID_MOVE, result);
  }

  @Test
  public void playGameReturnsInvalidMoveIfMoveOutsideRange() {
    // GIVEN
    Game game = getSampleGame(1L);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    game.setFirstToMove(USER1);

    // WHEN
    MoveResult result = gameService.playGame(game, USER1, 10);

    // THEN
    assertEquals(MoveResult.INVALID_MOVE, result);
  }

  @Test
  public void playGameReturnsInvalidMoveIfMoveTaken() {
    // GIVEN
    int[] moves = {1, 0, 0, 0, 0, 0, 0, 0, 0};
    Game game = getSampleGame(1L);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    game.setFirstToMove(USER1);
    game.setMoves(moves);

    // WHEN
    MoveResult result = gameService.playGame(game, USER1, 1);

    // THEN
    assertEquals(MoveResult.INVALID_MOVE, result);
  }

  @Test
  public void playGameShouldMarkUsersMoveAndChangeTurnAndReturnValidMoveIfNoOneWinsOrTies() {
    // GIVEN
    int move = 1;
    Game game = getSampleGame(1L);
    game.setGameStatus(GameStatus.IN_PROGRESS);

    // WHEN
    MoveResult result = gameService.playGame(game, USER1, move);

    // THEN
    assertEquals(USER1_MARK, game.getMoves()[move - 1]);
    assertEquals(USER2, game.getFirstToMove());
    assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    assertEquals(MoveResult.VALID_MOVE, result);
  }

  @Test
  public void playGameShouldDetectUser1Won() {
    // GIVEN
    int[] moves = {USER1_MARK, USER1_MARK, 0, 0, 0, 0, 0, 0, 0};
    Game game = getSampleGame(1L);
    game.setMoves(moves);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    game.setFirstToMove(USER1);

    // WHEN
    MoveResult result = gameService.playGame(game, USER1, 3);

    // THEN
    assertEquals(MoveResult.USER1_WON, result);
    assertEquals(GameStatus.USER1_WON, game.getGameStatus());
  }

  @Test
  public void playGameShouldDetectUser2Won() {
    // GIVEN
    int[] moves = {USER2_MARK, USER2_MARK, 0, 0, 0, 0, 0, 0, 0};
    Game game = getSampleGame(1L);
    game.setMoves(moves);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    game.setFirstToMove(USER2);

    // WHEN
    MoveResult result = gameService.playGame(game, USER2, 3);

    // THEN
    assertEquals(MoveResult.USER2_WON, result);
    assertEquals(GameStatus.USER2_WON, game.getGameStatus());
  }

  @Test
  public void playGameShouldDetectTie() {
    // GIVEN
    int[] moves = {USER1_MARK, USER2_MARK, USER1_MARK,
                   USER1_MARK, USER2_MARK, USER1_MARK,
                   USER2_MARK, USER1_MARK, 0};
    Game game = getSampleGame(1L);
    game.setMoves(moves);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    game.setFirstToMove(USER2);

    // WHEN
    MoveResult result = gameService.playGame(game, USER2, 9);

    // THEN
    assertEquals(MoveResult.TIE, result);
    assertEquals(GameStatus.TIE, game.getGameStatus());
  }

  @Test
  public void playGameShouldDetectAllEndConditions() {
    int[][] endConditions = {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9},
      {1, 4, 7},
      {2, 5, 8},
      {3, 6, 9},
      {1, 5, 9},
      {3, 5, 7}
    };
    for (int i = 0; i < 8; i++) {
      // GIVEN
      int[] moves = new int[9];
      for (int j = 0; j < 2; j++) {
        moves[endConditions[i][j] - 1] = USER1_MARK;
      }
      Game game = getSampleGame(1L);
      game.setMoves(moves);
      game.setGameStatus(GameStatus.IN_PROGRESS);

      // WHEN
      MoveResult result = gameService.playGame(game, USER1, endConditions[i][2]);

      // THEN
      assertEquals(MoveResult.USER1_WON, result);
      assertEquals(GameStatus.USER1_WON, game.getGameStatus());
    }
  }

  @Test
  public void joinGameShouldReturnFalseIfGameIsFull() {
    // GIVEN
    Game game = getSampleGame(1L);

    // WHEN
    boolean result = gameService.joinGame(game, USER1);

    // THEN
    assertFalse(result);
  }

  @Test
  public void joinGameShouldReturnFalseIfGameInProgressOrDone() {
    // GIVEN
    Game game = getSampleGame(1L);
    game.setUser2(null);
    game.setGameStatus(GameStatus.IN_PROGRESS);

    // WHEN
    boolean result = gameService.joinGame(game, USER1);

    // THEN
    assertFalse(result);
  }

  @Test
  public void joinGameShouldSetOpponentAndStatus() {
    // GIVEN
    Game game = getSampleGame(1L);
    game.setUser2(null);
    game.setFirstToMove(null);

    // WHEN
    boolean result = gameService.joinGame(game, USER2);

    // THEN
    assertEquals(USER2, game.getUser2());
    assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    assertTrue(result);
  }

  private Game getSampleGame(Long id) {
    return Game.builder()
      .id(id)
      .created(new Timestamp(System.currentTimeMillis()))
      .user1(USER1)
      .user2(USER2)
      .firstToMove(USER1)
      .moves(MOVES)
      .gameStatus(GAME_STATUS).build();
  }
}
