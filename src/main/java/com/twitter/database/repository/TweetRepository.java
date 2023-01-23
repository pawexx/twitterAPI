package com.twitter.database.repository;

import com.twitter.database.model.TweetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TweetRepository extends JpaRepository<TweetEntity, UUID> {
    @Query("select max(tweet.createdAt) from TweetEntity tweet where tweet.twitterUser.id = :userId")
    Optional<LocalDateTime> findLatestTweetDate(Long userId);
}
