package com.ug.grupa2.tictactoe.controllers;

import com.ug.grupa2.tictactoe.entities.Game;
import com.ug.grupa2.tictactoe.enums.MoveResult;
import com.ug.grupa2.tictactoe.services.GameService;
import com.ug.grupa2.tictactoe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {
  private final GameService gameService;
  private final UserService userService;

  @Autowired
  public GameController(GameService gameService, UserService userService) {
    this.gameService = gameService;
    this.userService = userService;
  }

  @GetMapping()
  @ResponseBody
  public List<Game> get(@RequestParam(required = false, defaultValue = "") String status) {
    if (!status.equals(""))
      return this.gameService.getGamesByStatusAndNotPrivate(status);
    else
      return this.gameService.getGames();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Game getGame(@PathVariable("id") Long id) {
    return this.gameService.getGameById(id);
  }


  @RequestMapping(value = "create", method = RequestMethod.PUT,
    produces = "text/plain")
  @ResponseBody
  public ResponseEntity<String> createGame(@RequestParam String user, @RequestParam boolean privateGame,
                                           @RequestParam(required = false) String user2) {
    Game game;
    if (privateGame) {
      try {
        this.userService.loadUserByUsername(user2); // no throw = ok
        game = this.gameService.createGame(user, privateGame);
        this.gameService.joinGame(game, user2);
      } catch (UsernameNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } else
      game = this.gameService.createGame(user, privateGame);

    return new ResponseEntity<String>(game.getId().toString(), HttpStatus.OK);
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
