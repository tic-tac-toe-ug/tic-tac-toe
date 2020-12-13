package com.ug.grupa2.tictactoe.entities;

  import lombok.AllArgsConstructor;
  import lombok.Getter;
  import lombok.NoArgsConstructor;
  import lombok.Setter;
  import org.hibernate.annotations.CreationTimestamp;

  import javax.persistence.*;
  import java.sql.Array;
  import java.sql.Timestamp;

@Entity
@Table(name = "games")
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

  //TODO: Check id strategy, "TABLE" causing some hibernate errors. I dont know why :( /Przemek
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;
  @CreationTimestamp
  @Getter private Timestamp created;
  @Getter @Setter private String user1;
  @Getter @Setter private String user2;
  @Getter @Setter private String firstToMove;
  @Getter @Setter private Integer[] moves;

  public GameEntity(String user1) {
    this.user1 = user1;
  }
}
