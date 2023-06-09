package com.restapi.todolist.repository;

import com.restapi.todolist.models.RefreshToken;
import com.restapi.todolist.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    @Modifying
    void deleteByUser(User user);
}
