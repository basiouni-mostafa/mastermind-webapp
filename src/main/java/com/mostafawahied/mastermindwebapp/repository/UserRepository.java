package com.mostafawahied.mastermindwebapp.repository;

import com.mostafawahied.mastermindwebapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional<User> findByEmail(String email);
//    Optional<User> findByUsername(String username);
    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByResetPasswordToken(String token);

    Optional<User> findByEmailOrUsername(String email, String username);

    List<User> findByOrderByScoreDesc();

}