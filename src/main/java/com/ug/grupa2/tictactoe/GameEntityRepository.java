package com.ug.grupa2.tictactoe;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.ug.grupa2.tictactoe.entities.Game;
import com.ug.grupa2.tictactoe.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameEntityRepository extends JpaRepository<Game, Long> {
  List<Game> findByGameStatusAndPrivateGame(GameStatus status, Boolean isPrivate);
}
