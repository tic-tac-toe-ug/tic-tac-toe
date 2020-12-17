package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.GameEntityRepository;
import com.ug.grupa2.tictactoe.entities.GameEntity;
import com.ug.grupa2.tictactoe.enums.GameStatus;
import com.ug.grupa2.tictactoe.enums.MoveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {

  private final GameEntityRepository gameRepository;

  public GameEntity createGame(String user1) {
    GameEntity newGame = new GameEntity(user1);
    Integer[] moves = new Integer[9];
    Arrays.fill(moves, 0);
    newGame.setMoves(moves);
    newGame.setGameStatus(GameStatus.CREATED);
    this.saveGame(newGame);
    return newGame;
  }

  public List<GameEntity> getGames() {
    return this.gameRepository.findAll();
  }

  public void deleteGame(Long id) {
    GameEntity gameEntity = getGameById(id);
    this.gameRepository.delete(gameEntity);
  }

  public MoveResult playGame(GameEntity gameEntity, String userId, Integer move) {
    MoveResult result = isMoveValid(gameEntity, userId, move);
    // If the requested move is invalid, return bad request.
    if (result != MoveResult.VALID_MOVE) {
      return result;
    }
    Integer[] moves = gameEntity.getMoves();
    // Mark the player's move
    // Player 1 -> 1
    // Player 2 -> 2
    if (gameEntity.getUser1().equals(userId)) {
      moves[move - 1] = 1;
      gameEntity.setFirstToMove(gameEntity.getUser2());
    } else {
      moves[move - 1] = 2;
      gameEntity.setFirstToMove(gameEntity.getUser1());
    }
    gameEntity.setMoves(moves);

    MoveResult gameResult = getGameResult(gameEntity);
    this.saveGame(gameEntity);
    return gameResult;
  }

  public boolean joinGame(GameEntity gameEntity, String userId) {

    // If the game is full, return 400
    if (gameEntity.getUser2() != null) {
      return false;
    }

    // If game is in progress or done, return 400
    if (gameEntity.getGameStatus() != GameStatus.CREATED) {
      return false;
    }

    // Set this user as an opponent in the game
    gameEntity.setUser2(userId);

    // Randomly select starting person
    Random generator = new Random();
    if (generator.nextInt(2) == 0)
      gameEntity.setFirstToMove(gameEntity.getUser1());
    else
      gameEntity.setFirstToMove(userId);
    gameEntity.setGameStatus(GameStatus.IN_PROGRESS);
    this.saveGame(gameEntity);
    return true;
  }

  public GameEntity getGameById(Long id) {
    Optional<GameEntity> optGame = this.gameRepository.findById(id);
    if (!optGame.isPresent()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND, "game not found"
      );
    }
    return optGame.get();
  }

  public void saveGame(GameEntity game) {
    this.gameRepository.save(game);
  }

  private MoveResult getGameResult(GameEntity gameEntity) {
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
    Integer[] moves = gameEntity.getMoves();
    for (int i = 0; i < 8; i++) {
      if (moves[endConditions[i][0] - 1] == 1 && moves[endConditions[i][1] - 1] == 1 && moves[endConditions[i][2] - 1] == 1) {
        gameEntity.setGameStatus(GameStatus.USER1_WON);
        return MoveResult.USER1_WON;
      } else if (moves[endConditions[i][0] - 1] == 2 && moves[endConditions[i][1] - 1] == 2 && moves[endConditions[i][2] - 1] == 2) {
        gameEntity.setGameStatus(GameStatus.USER2_WON);
        return MoveResult.USER2_WON;
      }
    }

    // look for tie
    if (!Arrays.asList(moves).contains(0)) {
      gameEntity.setGameStatus(GameStatus.TIE);
      return MoveResult.TIE;
    }
    return MoveResult.VALID_MOVE;
  }

  private MoveResult isMoveValid(GameEntity gameEntity, String userId, Integer move) {
    if (!Objects.equals(gameEntity.getUser1(), userId) && !Objects.equals(gameEntity.getUser2(), userId)) {
      return MoveResult.UNAUTHORIZED;
    }

    // As long as the game only has 1 person, it is impossible to play
    if (gameEntity.getUser1() == null || gameEntity.getUser2() == null) {
      return MoveResult.INVALID_MOVE;
    }

    // You can play only 'in progress' game
    if (gameEntity.getGameStatus() != GameStatus.IN_PROGRESS) {
      return MoveResult.INVALID_MOVE;
    }

    // If this is not userId's turn
    if (!gameEntity.getFirstToMove().equals(userId)) {
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
    Integer[] moves = gameEntity.getMoves();
    if (moves[move - 1] != 0) {
      return MoveResult.INVALID_MOVE;
    }

    return MoveResult.VALID_MOVE;
  }
}
