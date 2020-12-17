package com.ug.grupa2.tictactoe;

import com.ug.grupa2.tictactoe.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameEntityRepository extends JpaRepository<GameEntity, Long> {
}
