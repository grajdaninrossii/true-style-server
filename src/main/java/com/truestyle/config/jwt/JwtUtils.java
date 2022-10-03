package com.truestyle.config.jwt;

import com.truestyle.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:jwt.properties")
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationDay}")
    private int jwtExpirationDay;

    // Внутри authentication хранятся данные нашего пользователя
    public String generateJwtToken(@NonNull String username) {
//        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        final LocalDateTime now = LocalDateTime.now();

        // Используем имя пользователя + добавляем соль
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(Date.from(now.plusDays(jwtExpirationDay).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateJwtToken(@NonNull String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public String getUserNameFromJwtToken(@NonNull String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }
}
