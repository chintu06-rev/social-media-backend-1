package com.socialsync.repository;

import com.socialsync.model.PlatformType;
import com.socialsync.model.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    List<SocialAccount> findByActiveTrue();
    List<SocialAccount> findByUserIdAndActiveTrue(Long userId);
    List<SocialAccount> findByPlatformAndActiveTrue(PlatformType platform);
}
