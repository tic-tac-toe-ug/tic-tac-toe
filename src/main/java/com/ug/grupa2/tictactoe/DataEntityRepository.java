package com.ug.grupa2.tictactoe;

import com.ug.grupa2.tictactoe.entities.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEntityRepository extends JpaRepository<DataEntity, Long> {
}

