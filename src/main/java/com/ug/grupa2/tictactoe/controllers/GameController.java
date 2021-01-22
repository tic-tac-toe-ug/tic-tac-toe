package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.entities.Game;
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

  @GetMapping()
  @ResponseBody
  public List<Game> get(@RequestParam(required = false, defaultValue = "") String status) {
    if (!status.equals(""))
      return this.gameService.getGamesByStatus(status);
    else
      return this.gameService.getGames();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Game getGame(@PathVariable("id") Long id) {
    return this.gameService.getGameById(id);
  }


  @PutMapping("/create")
  @ResponseBody
  public ResponseEntity<Game> createGame(@RequestParam String user, @RequestParam boolean privateGame) {
    // create new game in database
    // If game is private, frontend should display URL with proper game ID, and then the invited user
    // can use this link to play. For now no user validation, anyone with link can join private game.
    // In the future we can provide some random number/string instead of plain ID so it's harder to spoof.
    return ResponseEntity.ok(this.gameService.createGame(user, privateGame));
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<String> deleteGame(@PathVariable("id") Long id) {
    this.gameService.deleteGame(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "{id}/join", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<String> joinGame(@PathVariable("id") Long id, @RequestParam String userId) {
    Game game = this.gameService.getGameById(id);
    if (this.gameService.joinGame(game, userId)) {
      return new ResponseEntity<String>(HttpStatus.OK);
    } else {
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "{id}/play", method = RequestMethod.POST,
    produces = "text/plain")
  @ResponseBody
  public ResponseEntity<String> playGame(@PathVariable("id") Long id, @RequestParam String userId, @RequestParam Integer move) {
    Game game = this.gameService.getGameById(id);
    MoveResult result = this.gameService.playGame(game, userId, move);
    return result.toResponse();
  }
}
