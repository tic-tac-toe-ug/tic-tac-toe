package com.ug.grupa2.tictactoe.entities;

import com.ug.grupa2.tictactoe.enums.GameStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "games")
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  private Long id;
  @CreationTimestamp
  @Getter
  private Timestamp created;
  @Getter
  @Setter
  // TODO: add field validation
  private String user1;
  @Getter
  @Setter
  private String user2;
  @Getter
  @Setter
  private String firstToMove;
  @Column(name = "moves")
  @Getter
  @Setter
  private int[] moves;
  @Getter
  @Setter
  private GameStatus gameStatus;

  public Game(String user1) {
    this.user1 = user1;
    this.moves = new int[9];
    this.gameStatus = GameStatus.CREATED;
  }
}
