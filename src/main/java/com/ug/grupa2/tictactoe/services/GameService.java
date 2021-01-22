package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.GameEntityRepository;
import com.ug.grupa2.tictactoe.entities.Game;
import com.ug.grupa2.tictactoe.enums.GameStatus;
import com.ug.grupa2.tictactoe.enums.MoveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {

  private static final EnumSet<MoveResult> END_GAME_MOVES = EnumSet.of(MoveResult.TIE, MoveResult.USER1_WON,
    MoveResult.USER2_WON);

  private final GameEntityRepository gameRepository;
  private final UserService userService;

  public Game createGame(String user1, boolean privateGame) {
    Game newGame = new Game(user1, privateGame);
    this.saveGame(newGame);
    return newGame;
  }

  public List<Game> getGames() {
    return this.gameRepository.findAll();
  }

  public List<Game> getGamesByStatusAndNotPrivate(String status) {
    if (status.equals("created"))
      return this.gameRepository.findByGameStatusAndPrivateGame(GameStatus.CREATED, false);
    else
      return this.gameRepository.findAll();

  }

  public void deleteGame(Long id) {
    Game game = getGameById(id);
    this.gameRepository.delete(game);
  }

  public MoveResult playGame(Game game, String userId, Integer move) {
    MoveResult result = isMoveValid(game, userId, move);
    // If the requested move is invalid, return bad request.
    if (result != MoveResult.VALID_MOVE) {
      return result;
    }
    int[] moves = game.getMoves();
    // Mark the player's move
    // Player 1 -> 1
    // Player 2 -> 2
    if (game.getUser1().equals(userId)) {
      moves[move - 1] = 1;
      game.setFirstToMove(game.getUser2());
    } else {
      moves[move - 1] = 2;
      game.setFirstToMove(game.getUser1());
    }
    game.setMoves(moves);

    MoveResult gameResult = getGameResult(game);

    if (END_GAME_MOVES.contains(gameResult)) {
      userService.updateUsersScore(gameResult, game.getUser1(), game.getUser2());
    }

    this.saveGame(game);
    return gameResult;
  }

  public boolean joinGame(Game game, String userId) {
    try {
      userService.loadUserByUsername(userId); // no exception = ok
    } catch (UsernameNotFoundException e) {
      return false;
    }
    // If the game is full, return 400
    if (game.getUser2() != null) {
      return false;
    }

    // If game is in progress or done, return 400
    if (game.getGameStatus() != GameStatus.CREATED) {
      return false;
    }

    // Set this user as an opponent in the game
    game.setUser2(userId);

    // Randomly select starting person
    Random generator = new Random();
    if (generator.nextInt(2) == 0)
      game.setFirstToMove(game.getUser1());
    else
      game.setFirstToMove(userId);
    game.setGameStatus(GameStatus.IN_PROGRESS);
    this.saveGame(game);
    return true;
  }

  public Game getGameById(Long id) {
    Optional<Game> optGame = this.gameRepository.findById(id);
    return optGame.orElseThrow(() -> new ResponseStatusException(
      HttpStatus.NOT_FOUND, "game not found"
    ));
  }

  public void saveGame(Game game) {
    this.gameRepository.save(game);
  }

  private MoveResult getGameResult(Game game) {
    // The easiest way is to check all possibilities, since it's 3x3 - we can afford it
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
    int[] moves = game.getMoves();
    for (int i = 0; i < 8; i++) {
      if (moves[endConditions[i][0] - 1] == 1 && moves[endConditions[i][1] - 1] == 1 && moves[endConditions[i][2] - 1] == 1) {
        game.setGameStatus(GameStatus.USER1_WON);
        return MoveResult.USER1_WON;
      } else if (moves[endConditions[i][0] - 1] == 2 && moves[endConditions[i][1] - 1] == 2 && moves[endConditions[i][2] - 1] == 2) {
        game.setGameStatus(GameStatus.USER2_WON);
        return MoveResult.USER2_WON;
      }
    }

    // look for tie
    if (IntStream.of(moves).noneMatch(x -> x == 0)) {
      game.setGameStatus(GameStatus.TIE);
      return MoveResult.TIE;
    }
    return MoveResult.VALID_MOVE;
  }

  private MoveResult isMoveValid(Game game, String userId, Integer move) {
    if (!Objects.equals(game.getUser1(), userId) && !Objects.equals(game.getUser2(), userId)) {
      return MoveResult.UNAUTHORIZED;
    }

    // As long as the game only has 1 person, it is impossible to play
    if (game.getUser1() == null || game.getUser2() == null) {
      return MoveResult.INVALID_MOVE;
    }

    // You can play only 'in progress' game
    if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
      return MoveResult.INVALID_MOVE;
    }

    // If this is not userId's turn
    if (!game.getFirstToMove().equals(userId)) {
      return MoveResult.INVALID_MOVE;
    }
    // Each element in moves array represents specific player's move.
    // Fields in 3x3 tic tac toe grid are numbered as follows:
    // | 1 | 2 | 3 |
    // | 4 | 5 | 6 |
    // | 7 | 8 | 9 |
    if (move < 1 || move > 9) {
      return MoveResult.INVALID_MOVE;
    }

    // Check if this move is possible
    int[] moves = game.getMoves();
    if (moves[move - 1] != 0) {
      return MoveResult.INVALID_MOVE;
    }

    return MoveResult.VALID_MOVE;
  }

}
