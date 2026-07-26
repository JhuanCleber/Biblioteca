package com.matemagicos.biblioteca.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Gera um token JWT contendo o email (subject) e o id do usuário (claim extra).
     */
    public String gerarToken(Integer idUsuario, String email) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("idUsuario", idUsuario)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSigningKey())
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairTodasClaims(token).getSubject();
    }

    public Integer extrairIdUsuario(String token) {
        return extrairTodasClaims(token).get("idUsuario", Integer.class);
    }

    /**
     * Retorna true se o token for válido (assinatura correta e não expirado).
     * Qualquer problema (assinatura inválida, token expirado, formato errado)
     * cai no catch e o token é tratado como inválido.
     */
    public boolean tokenValido(String token) {
        try {
            extrairTodasClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extrairTodasClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
