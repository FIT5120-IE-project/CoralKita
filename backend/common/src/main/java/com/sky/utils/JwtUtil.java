package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * Generate JWT
     * Uses HS256 algorithm with fixed secret key
     *
     * @param secretKey JWT secret key
     * @param ttlMillis JWT expiration time (milliseconds)
     * @param claims    Information to set
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // Specify the signature algorithm to use, which is the header part
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // Generate JWT expiration time
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // Set JWT body
        JwtBuilder builder = Jwts.builder()
                // If there are private claims, they must be set first, otherwise they will override standard claims
                .setClaims(claims)
                // Set signature algorithm and secret key
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // Set expiration time
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Decrypt token
     *
     * @param secretKey JWT secret key. This key must be kept secure on the server and not exposed, otherwise the signature can be forged
     * @param token     Encrypted token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // Get DefaultJwtParser
        Claims claims = Jwts.parser()
                // Set signature secret key
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // Parse JWT
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
