package com.poshanp.task.manager.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.poshanp.task.manager.application.services.interfaces.ITokenService;
import com.poshanp.task.manager.domain.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProviderImpl implements ITokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}") // e.g., 900000 (15 mins)
    private long jwtExpirationMs;

    @Value("${jwt.refresh.expiration}") // e.g., 604800000 (7 days)
    private long refreshExpirationMs;

    private static final String ISSUER = "TASK MANAGER APPLICATION";
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_TYPE = "tokenType";
    private static final String TYPE_ACCESS = "ACCESS";
    private static final String TYPE_REFRESH = "REFRESH";

    @Override
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        try {
            return JWT.create()
                    .withSubject(String.valueOf(user.getId()))
                    .withClaim(CLAIM_USER_ID, user.getId())
                    .withClaim("username", user.getUsername())
                    .withClaim(CLAIM_TYPE, TYPE_ACCESS)
                    .withIssuer(ISSUER)
                    .withIssuedAt(now)
                    .withExpiresAt(expiryDate)
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationMs);

        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim(CLAIM_USER_ID, user.getId())
                .withClaim(CLAIM_TYPE, TYPE_REFRESH)
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    @Override
    public Long validateAccessTokenAndGetUserId(String token) {
        try {
            DecodedJWT decodedJWT = getVerifier().verify(token);

            String type = decodedJWT.getClaim(CLAIM_TYPE).asString();
            if (!TYPE_ACCESS.equals(type)) {
                return null;
            }

            return decodedJWT.getClaim(CLAIM_USER_ID).asLong();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long validateRefreshTokenAndGetUserId(String token) {
        try {
            DecodedJWT decodedJWT = getVerifier().verify(token);

            String type = decodedJWT.getClaim(CLAIM_TYPE).asString();
            if (!TYPE_REFRESH.equals(type)) {
                return null;
            }

            return decodedJWT.getClaim(CLAIM_USER_ID).asLong();
        } catch (Exception e) {
            return null;
        }
    }

    private JWTVerifier getVerifier() {
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .withIssuer(ISSUER)
                .build();
    }
}
