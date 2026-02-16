package com.poshanp.task.manager.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.poshanp.task.manager.application.exceptions.TokenGenerationException;
import com.poshanp.task.manager.application.exceptions.UnauthorizedException;
import com.poshanp.task.manager.application.repositories.IRefreshTokenRepository;
import com.poshanp.task.manager.application.services.interfaces.ITokenService;
import com.poshanp.task.manager.domain.constants.ErrorMessage;
import com.poshanp.task.manager.domain.entities.RefreshToken;
import com.poshanp.task.manager.domain.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class TokenServiceImpl implements ITokenService {

    private final IRefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}") // e.g., 900000 (15 mins)
    private long jwtExpirationMs;

    @Value("${jwt.refresh.cookie.max-age}") // e.g., 604800000 (7 days)
    private long refreshExpirationMs;

    @Value("${jwt.max-refresh-tokens-per-user:5}") // Max active refresh tokens per user
    private int maxRefreshTokensPerUser;

    private static final String ISSUER = "TASK MANAGER APPLICATION";
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_TYPE = "tokenType";
    private static final String TYPE_ACCESS = "ACCESS";

    public TokenServiceImpl(IRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateAccessToken(User user) {
        if (user == null) {
            log.error("Cannot generate access token for null user");
            throw new TokenGenerationException(ErrorMessage.TOKEN_INVALID_USER_DATA);
        }

        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            return JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withSubject(String.valueOf(user.getId()))
                    .withClaim(CLAIM_USER_ID, user.getId())
                    .withClaim("username", user.getUsername())
                    .withClaim(CLAIM_TYPE, TYPE_ACCESS)
                    .withIssuer(ISSUER)
                    .withIssuedAt(now)
                    .withExpiresAt(expiryDate)
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (Exception e) {
            log.error("Failed to generate access token for user: {}", user.getId(), e);
            throw new TokenGenerationException(ErrorMessage.TOKEN_GENERATION_FAILED);
        }
    }

    @Override
    public Long validateAccessTokenAndGetUserId(String token) {
        if (token == null || token.isBlank()) {
            log.debug("Token validation failed: token is null or empty");
            return null;
        }

        try {
            DecodedJWT decodedJWT = getVerifier().verify(token);
            String type = decodedJWT.getClaim(CLAIM_TYPE).asString();

            if (!TYPE_ACCESS.equals(type)) {
                log.debug("Token validation failed: incorrect token type");
                return null;
            }

            return decodedJWT.getClaim(CLAIM_USER_ID).asLong();
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isAccessTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.debug("Failed to check token expiration: {}", e.getMessage());
            return true;
        }
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        if (user == null) {
            log.error("Cannot create refresh token for null user");
            throw new TokenGenerationException(ErrorMessage.TOKEN_INVALID_USER_DATA);
        }

        try {
            int activeTokenCount = refreshTokenRepository.countActiveTokensByUserId(user.getId());
            if (activeTokenCount >= maxRefreshTokensPerUser) {
                log.warn("User {} has {} active tokens, revoking oldest",
                        user.getId(), activeTokenCount);
                revokeOldestTokensForUser(user.getId(), activeTokenCount - maxRefreshTokensPerUser + 1);
            }

            String tokenString = UUID.randomUUID().toString();
            RefreshToken refreshToken = RefreshToken.create(
                    tokenString,
                    user,
                    refreshExpirationMs
            );

            refreshToken = refreshTokenRepository.save(refreshToken);

            log.debug("Refresh token created for user: {}", user.getUsername());
            return refreshToken;

        } catch (Exception e) {
            log.error("Failed to create refresh token for user: {}", user.getId(), e);
            throw new TokenGenerationException(ErrorMessage.TOKEN_GENERATION_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String token) {
        if (token == null || token.isBlank()) {
            log.debug("Refresh token validation failed: token is null or empty");
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_TOKEN);
        }

        try {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

            if (refreshToken == null) {
                log.warn("Refresh token not found in database");
                throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_TOKEN);
            }

            if (!refreshToken.isValid()) {
                log.warn("Refresh token is invalid (revoked or expired): {}", token);
                throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_TOKEN);
            }

            return refreshToken;

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error validating refresh token", e);
            throw new UnauthorizedException(ErrorMessage.AUTH_INVALID_TOKEN);
        }
    }

    @Override
    @Transactional
    public RefreshToken rotateRefreshToken(String oldToken) {
        try {
            RefreshToken oldRefreshToken = validateRefreshToken(oldToken);

            RefreshToken newRefreshToken = createRefreshToken(oldRefreshToken.getUser());
            oldRefreshToken.revoke(newRefreshToken.getToken());
            refreshTokenRepository.save(oldRefreshToken);

            log.info("Refresh token rotated for user: {}", oldRefreshToken.getUser().getUsername());
            return newRefreshToken;

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error rotating refresh token", e);
            throw new TokenGenerationException(ErrorMessage.TOKEN_GENERATION_FAILED);
        }
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        try {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

            if (refreshToken != null && !refreshToken.isRevoked()) {
                refreshToken.revoke(null);
                refreshTokenRepository.save(refreshToken);
                log.debug("Refresh token revoked: {}", token);
            }

        } catch (Exception e) {
            log.error("Error revoking refresh token", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void revokeAllUserRefreshTokens(Long userId) {
        try {
            var tokens = refreshTokenRepository.findAllByUserId(userId);

            tokens.stream()
                    .filter(token -> !token.isRevoked())
                    .forEach(token -> {
                        token.revoke(null);
                        refreshTokenRepository.save(token);
                    });

            log.info("All refresh tokens revoked for user: {}", userId);

        } catch (Exception e) {
            log.error("Error revoking all user tokens for user: {}", userId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            refreshTokenRepository.deleteExpiredTokens();
            log.info("Expired refresh tokens cleaned up");
        } catch (Exception e) {
            log.error("Error cleaning up expired tokens", e);
            throw e;
        }
    }


    @Override
    public String extractTokenId(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getId();
        } catch (Exception e) {
            log.debug("Failed to extract token ID: {}", e.getMessage());
            return null;
        }
    }

    private JWTVerifier getVerifier() {
        try {
            return JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(ISSUER)
                    .build();
        } catch (Exception e) {
            log.error("Failed to create JWT verifier", e);
            throw new TokenGenerationException(ErrorMessage.TOKEN_GENERATION_FAILED);
        }
    }

    private void revokeOldestTokensForUser(Long userId, int count) {
        var tokens = refreshTokenRepository.findAllByUserId(userId);

        tokens.stream()
                .filter(token -> !token.isRevoked())
                .sorted((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                .limit(count)
                .forEach(token -> {
                    token.revoke(null);
                    refreshTokenRepository.save(token);
                    log.debug("Revoked old token for user: {}", userId);
                });
    }
}