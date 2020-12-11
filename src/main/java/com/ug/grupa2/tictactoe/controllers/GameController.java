package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.GameEntityRepository;
import com.ug.grupa2.tictactoe.entities.GameEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/games")
public class GameController {
  private final GameEntityRepository repository;

  public GameController(GameEntityRepository repository) {
    this.repository = repository;
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
}
