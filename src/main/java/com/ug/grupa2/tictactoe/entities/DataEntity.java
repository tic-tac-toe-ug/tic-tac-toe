package com.ug.grupa2.tictactoe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "fun_data")
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class DataEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Integer id;
  private String content;
}
