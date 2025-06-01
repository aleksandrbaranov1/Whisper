package org.example.whisper.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.whisper.Security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final String SECRET_KEY = "kLsDJtVVtRst1AymjSOcI4etCfgvdZBk6nYY8bdaV/8=";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(((MyUserDetails) userDetails).getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 час
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean validateToken(String token, MyUserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
