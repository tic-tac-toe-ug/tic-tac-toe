package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.entities.GameEntity;
import com.ug.grupa2.tictactoe.enums.MoveResult;
import com.ug.grupa2.tictactoe.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public List<GameEntity> get() {

    return this.gameService.getGames();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public GameEntity getGame(@PathVariable("id") Long id) {
    return this.gameService.getGameById(id);
  }


  @PutMapping("/create")
  @ResponseBody
  public GameEntity createGame(@RequestParam String user) {
    // create new game in database
    return this.gameService.createGame(user);
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
  public ResponseEntity<String> joinGame(@PathVariable("id") Long id, @RequestParam String userId) {
    GameEntity gameEntity = this.gameService.getGameById(id);
    if (this.gameService.joinGame(gameEntity, userId)){
      return new ResponseEntity<String>(HttpStatus.OK);
    }
    else{
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "{id}/play", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> playGame(@PathVariable("id") Long id, @RequestParam String userId, @RequestParam Integer move) {
    GameEntity gameEntity = this.gameService.getGameById(id);
    MoveResult result = this.gameService.playGame(gameEntity, userId, move);
    return result.toResponse();
  }
}
