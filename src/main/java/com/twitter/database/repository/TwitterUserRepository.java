package com.twitter.database.repository;

import com.twitter.database.model.TwitterUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TwitterUserRepository extends JpaRepository<TwitterUserEntity, UUID> {
    Optional<TwitterUserEntity> findByName(String name);
}
