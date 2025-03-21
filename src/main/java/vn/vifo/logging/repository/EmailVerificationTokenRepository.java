package vn.vifo.logging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<vn.vifo.logging.entity.EmailVerificationToken, UUID> {
    Optional<vn.vifo.logging.entity.EmailVerificationToken> findByUserId(UUID userId);

    Optional<vn.vifo.logging.entity.EmailVerificationToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken rt WHERE rt.user.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}
