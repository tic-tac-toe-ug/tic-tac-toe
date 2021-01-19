package com.ug.grupa2.tictactoe;

import com.ug.grupa2.tictactoe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsernameOrEmail(String login, String email);

  User findByUsername(String username);

  List<User> findByOrderByScoreDesc();
}
