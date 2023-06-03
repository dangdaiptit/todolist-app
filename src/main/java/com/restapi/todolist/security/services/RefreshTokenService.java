package com.restapi.todolist.security.services;

import com.restapi.todolist.exception.TokenRefreshException;
import com.restapi.todolist.models.RefreshToken;
import com.restapi.todolist.models.users.User;
import com.restapi.todolist.repository.RefreshTokenRepository;
import com.restapi.todolist.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    final RefreshTokenRepository refreshTokenRepository;
    final UserRepository userRepository;
    @Value("${todolist.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findRefreshTokenByToken(token);
    }


    public RefreshToken createRefreshToken(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Not found user with userId: " + userId));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }
        return token;
    }


    @Transactional
    public void delelteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Not found user with userId: " + userId));
        refreshTokenRepository.deleteByUser(user);
    }


}
