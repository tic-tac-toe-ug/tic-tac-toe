package com.ug.grupa2.tictactoe;

import com.ug.grupa2.tictactoe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByLoginOrEmail(String login, String email);

}