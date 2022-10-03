package com.truestyle.repository.user;

import com.truestyle.entity.user.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    void deleteByToken(String token);

    boolean existsByToken(String token);
}
