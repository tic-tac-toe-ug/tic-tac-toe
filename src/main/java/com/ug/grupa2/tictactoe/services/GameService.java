package com.ug.grupa2.tictactoe.services;

import com.ug.grupa2.tictactoe.GameEntityRepository;
import com.ug.grupa2.tictactoe.entities.GameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class GameService {
  public enum GameStatus {
    CREATED("Created"),
    IN_PROGRESS("In progress"),
    TIE("Tie"),
    DONE1("Finished. User1 won"),
    DONE2("Finished. User2 won");
    private final String name;

    GameStatus(String s) {
      name = s;
    }

  }

  private final GameEntityRepository gameRepository;

  @Autowired
  public GameService(GameEntityRepository gameRepository) {

    this.gameRepository = gameRepository;
  }

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

  public ResponseEntity<String> playGame(GameEntity gameEntity, String userId, Integer move) {
    HttpStatus status = isMoveValid(gameEntity, userId, move);
    // If the requested move is invalid, return bad request.
    if (status != HttpStatus.OK) {
      return new ResponseEntity<>(status);
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

    // Check if this move leads to end of the game
    Optional<ResponseEntity> result = getGameResult(gameEntity);
    this.saveGame(gameEntity);

    if (result.isPresent()) {
      return result.get();
    } else {
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  public HttpStatus joinGame(GameEntity gameEntity, String userId) {

    // If the game is full, return 400
    if (gameEntity.getUser2() != null) {
      return HttpStatus.BAD_REQUEST;
    }

    // If game is in progress or done, return 400
    if (gameEntity.getGameStatus() != GameStatus.CREATED) {
      return HttpStatus.BAD_REQUEST;
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
    return HttpStatus.OK;
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

  public Map<String, Object> gameToModel(GameEntity game) {
    Map<String, Object> temp = new HashMap<>();
    temp.put("id", game.getId().toString());
    temp.put("user1", game.getUser1());
    temp.put("user2", game.getUser2());
    temp.put("firstMove", game.getFirstToMove());
    temp.put("moves", game.getMoves());
    temp.put("time", game.getCreated().toString());
    temp.put("status", game.getGameStatus().toString());
    return temp;
  }

  public List<Map<String, Object>> getGamesAsModel() {
    List<GameEntity> gameEntities = this.getGames();
    List<Map<String, Object>> model = new ArrayList<>();

    for (GameEntity e : gameEntities) {
      Map<String, Object> temp = gameToModel(e);
      model.add(temp);
    }
    return model;
  }

  public Map<String, Object> getGameByIdAsModel(Long id) {

    GameEntity gameEntity = this.getGameById(id);
    Map<String, Object> model = gameToModel(gameEntity);
    return model;
  }

  private Optional<ResponseEntity> getGameResult(GameEntity gameEntity) {
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
        gameEntity.setGameStatus(GameStatus.DONE1);
        return Optional.of(new ResponseEntity<String>("Player 1 won", HttpStatus.OK));
      } else if (moves[endConditions[i][0] - 1] == 2 && moves[endConditions[i][1] - 1] == 2 && moves[endConditions[i][2] - 1] == 2) {
        gameEntity.setGameStatus(GameStatus.DONE2);
        return Optional.of(new ResponseEntity<String>("Player 2 won", HttpStatus.OK));
      }
    }

    // look for tie
    if (!Arrays.asList(moves).contains(0)) {
      gameEntity.setGameStatus(GameStatus.TIE);
      return Optional.of(new ResponseEntity<>("Tie", HttpStatus.OK));
    }
    return Optional.empty();
  }

  private HttpStatus isMoveValid(GameEntity gameEntity, String userId, Integer move) {
    if (!Objects.equals(gameEntity.getUser1(), userId) && !Objects.equals(gameEntity.getUser2(), userId)) {
      return HttpStatus.UNAUTHORIZED;
    }

    // As long as the game only has 1 person, it is impossible to play
    if (gameEntity.getUser1() == null || gameEntity.getUser2() == null) {
      return HttpStatus.BAD_REQUEST;
    }

    // You can play only 'in progress' game
    if (gameEntity.getGameStatus() != GameStatus.IN_PROGRESS) {
      return HttpStatus.BAD_REQUEST;
    }

    // If this is not userId's turn
    if (!gameEntity.getFirstToMove().equals(userId)) {
      return HttpStatus.BAD_REQUEST;
    }
    // Each element in moves array represents specific player's move.
    // Fields in 3x3 tic tac toe grid are numbered as follows:
    // | 1 | 2 | 3 |
    // | 4 | 5 | 6 |
    // | 7 | 8 | 9 |
    if (move < 1 || move > 9) {
      return HttpStatus.BAD_REQUEST;
    }

    // Check if this move is possible
    Integer[] moves = gameEntity.getMoves();
    if (moves[move - 1] != 0) {
      return HttpStatus.BAD_REQUEST;
    }


    return HttpStatus.OK;
  }
}
