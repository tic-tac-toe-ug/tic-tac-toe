package com.ug.grupa2.tictactoe.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum MoveResult {
  USER1_WON,
  USER2_WON,
  TIE,
  VALID_MOVE,
  INVALID_MOVE,
  UNAUTHORIZED;

  public ResponseEntity<String> toResponse() {
    switch (this) {
      case INVALID_MOVE:
        return new ResponseEntity<>("Invalid move", HttpStatus.BAD_REQUEST);
      case UNAUTHORIZED:
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
      case USER1_WON:
        return new ResponseEntity<>("User1 won.", HttpStatus.OK);
      case USER2_WON:
        return new ResponseEntity<>("User2 won.", HttpStatus.OK);
      case TIE:
        return new ResponseEntity<>("Tie", HttpStatus.OK);
      default:
        return new ResponseEntity<>("Correct move", HttpStatus.OK);
    }
  }

}
//
//      case INVALID_MOVE:
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//  case UNAUTHORIZED:
//  return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//  case USER1_WON:
//  return new ResponseEntity<>("\"data\": \"User1 won\"", HttpStatus.OK);
//  case USER2_WON:
//  return new ResponseEntity<>("\"data\": \"User2 won\"", HttpStatus.OK);
//  case TIE:
//  return new ResponseEntity<>("\"data\": \"Tie\"", HttpStatus.OK);
//default:
//  return new ResponseEntity<>("\"data\": \"Correct move\"", HttpStatus.OK);
