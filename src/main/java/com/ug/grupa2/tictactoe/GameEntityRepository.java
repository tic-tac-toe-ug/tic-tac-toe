package com.ug.grupa2.tictactoe;

import com.ug.grupa2.tictactoe.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameEntityRepository extends JpaRepository<GameEntity, Long> {
}
