package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.entities.GameEntity;
import com.ug.grupa2.tictactoe.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/games")
public class GameController {
  private final GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("/")
  @ResponseBody
  public List<Map<String, Object>> get() {

    return this.gameService.getGamesAsModel();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Map<String, Object> getGame(@PathVariable("id") Long id) {
    return this.gameService.getGameByIdAsModel(id);
  }


  @PutMapping("/create")
  @ResponseBody
  public Map<String, Object> createGame(@RequestParam(required = true) String user) {
    // create new game in database
    GameEntity game = this.gameService.createGame(user);

    // return debug data
    Map<String, Object> model = this.gameService.gameToModel(game);
    return model;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<String> deleteGame(@PathVariable("id") Long id) {
    this.gameService.deleteGame(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO: update to support public/private games
  @RequestMapping(value = "{id}/join", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> joinGame(@PathVariable("id") Long id, @RequestParam(required = true) String userId) {
    GameEntity gameEntity = this.gameService.getGameById(id);
    return new ResponseEntity<String>(this.gameService.joinGame(gameEntity, userId));
  }

  @RequestMapping(value = "{id}/play", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> playGame(@PathVariable("id") Long id, @RequestParam(required = true) String userId, @RequestParam(required = true) Integer move) {
    GameEntity gameEntity = this.gameService.getGameById(id);
    return this.gameService.playGame(gameEntity, userId, move);
  }
}
