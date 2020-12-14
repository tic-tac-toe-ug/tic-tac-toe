package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.GameEntityRepository;
import com.ug.grupa2.tictactoe.entities.GameEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
@RequestMapping("/games")
public class GameController {
  private final GameEntityRepository repository;

  public GameController(GameEntityRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/")
  @ResponseBody
  public List<Map<String, Object>> get(){
    List<GameEntity> gameEntities = this.repository.findAll();
    List<Map<String, Object>> model = new ArrayList<>();

    for (GameEntity e : gameEntities) {
      Map<String, Object> temp = new HashMap<>();
      temp.put("id", e.getId().toString());
      temp.put("user1", e.getUser1());
      temp.put("user2", e.getUser2());
      temp.put("firstMove", e.getFirstToMove());
      temp.put("moves", e.getMoves());
      temp.put("time", e.getCreated().toString());
      model.add(temp);
    }

    return model;
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Map<String, Object> getGame(@PathVariable("id") Long id) {
    Optional<GameEntity> optGameEntity = this.repository.findById(id);
    if (!optGameEntity.isPresent()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND, "game not found"
      );
    }
    GameEntity gameEntity = optGameEntity.get();
    Map<String, Object> model = new HashMap<>();
    model.put("id", gameEntity.getId().toString());
    model.put("user1", gameEntity.getUser1());
    model.put("user2", gameEntity.getUser2());
    model.put("firstMove", gameEntity.getFirstToMove());
    model.put("time", gameEntity.getCreated().toString());
    return model;
  }

  //@PostMapping("/{id}}") doesn't work for me :(
  @RequestMapping(value = "{id}", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> updateGame(@PathVariable("id") Long id, @RequestParam(required = false) String user2, @RequestParam(required = false) Integer move) {
    // create new game in database
    Optional<GameEntity> optGameEntity = this.repository.findById(id);
    if (!optGameEntity.isPresent()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND, "game not found"
      );
    }
    GameEntity gameEntity = optGameEntity.get();
    if (user2 != null && gameEntity.getUser2() == null) {
      gameEntity.setUser2(user2);

      Random generator = new Random();
      if (generator.nextInt(2) == 0)
        gameEntity.setFirstToMove(gameEntity.getUser1());
      else
        gameEntity.setFirstToMove(user2);
    }
    // todo: implement moves in some way. Now: array of 9 ints, let's assume first move is circle
    this.repository.save(gameEntity);

    Map<String, Object> model = new HashMap<>();
    model.put("id", gameEntity.getId().toString());
    model.put("user1", gameEntity.getUser1());
    model.put("user2", gameEntity.getUser2());
    model.put("firstMove", gameEntity.getFirstToMove());
    model.put("time", gameEntity.getCreated().toString());
    return model;
  }

  @PutMapping("/create")
  @ResponseBody
  public Map<String, Object> createGame() {
    // create new game in database
    GameEntity newGame = new GameEntity("u1");
    Integer[] moves = new Integer[9];
    Arrays.fill(moves, 0);
    newGame.setMoves(moves);
    this.repository.save(newGame);

    // return debug data
    Map<String, Object> model = new HashMap<>();
    model.put("id", newGame.getId().toString());
    model.put("user1", newGame.getUser1());
    model.put("time", newGame.getCreated().toString());
    return model;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public HttpStatus deleteGame(@PathVariable("id") Long id) {
    Optional<GameEntity> optGameEntity = this.repository.findById(id);
    if (!optGameEntity.isPresent()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND, "game not found"
      );
    }
    GameEntity gameEntity = optGameEntity.get();
    this.repository.delete(gameEntity);
    return HttpStatus.OK;
  }

  // TODO: update to support public/private games
  // TODO: don't allow joining game that is already complete
  @RequestMapping(value = "{id}/join", method = RequestMethod.POST)
  @ResponseBody
  public HttpStatus joinGame(@PathVariable("id") Long id, @RequestParam(required = true) String userId) {
    Optional<GameEntity> optGameEntity = this.repository.findById(id);
    // If the game does not exist, return 404
    if (!optGameEntity.isPresent()) {
      return HttpStatus.NOT_FOUND;
    }

    // If the game is full, return 400
    GameEntity gameEntity = optGameEntity.get();
    if (gameEntity.getUser2() != null) {
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

    this.repository.save(gameEntity);
    return HttpStatus.OK;
  }

  // TODO: add handling of full board/game complete
  @RequestMapping(value = "{id}/play", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> playGame(@PathVariable("id") Long id, @RequestParam(required = true) String userId, @RequestParam(required = true) Integer move) {
    Optional<GameEntity> optGameEntity = this.repository.findById(id);
    // If the game does not exist, return 404
    if (!optGameEntity.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Check if userId is among players in the requested game
    GameEntity gameEntity = optGameEntity.get();
    if (!Objects.equals(gameEntity.getUser1(), userId) && !Objects.equals(gameEntity.getUser2(), userId)) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // As long as the game only has 1 person, it is impossible to play
    if (gameEntity.getUser1() == null || gameEntity.getUser2() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // If this is not userId's turn
    if(!gameEntity.getFirstToMove().equals(userId)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // If the requested move is invalid, return bad request.
    // Each element in moves array represents specific player's move.
    // Fields in 3x3 tic tac toe grid are numbered as follows:
    // | 1 | 2 | 3 |
    // | 4 | 5 | 6 |
    // | 7 | 8 | 9 |
    if (move < 1 || move > 9) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Check if this move is possible
    Integer[] moves = gameEntity.getMoves();
    if (moves[move - 1] != 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Mark the player's move
    // Player 1 -> 1
    // Player 2 -> 2
    if (gameEntity.getUser1().equals(userId)) {
      moves[move - 1] = 1;
      gameEntity.setFirstToMove(gameEntity.getUser2());
    }
    else {
      moves[move - 1] = 2;
      gameEntity.setFirstToMove(gameEntity.getUser1());
    }
    gameEntity.setMoves(moves);

    // Check if this move leads to end of the game
    // The easiest way is to check all possibilities, since it's 3x3 - we can afford it
    int[][] end_conditions = {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9},
      {1, 4, 7},
      {2, 5, 8},
      {3, 6, 9},
      {1, 5, 9},
      {3, 5, 7}
    };

    this.repository.save(gameEntity);

    for(int i = 0; i < 8; i++) {
      if (moves[end_conditions[i][0] - 1] == 1 && moves[end_conditions[i][1] - 1] == 1 && moves[end_conditions[i][2] - 1] == 1) {
        return new ResponseEntity<String>("Player 1 won", HttpStatus.OK);
      }
      else if (moves[end_conditions[i][0] - 1] == 2 && moves[end_conditions[i][1] - 1] == 2 && moves[end_conditions[i][2] - 1] == 2) {
        return new ResponseEntity<>("Player 2 won", HttpStatus.OK);
      }
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
