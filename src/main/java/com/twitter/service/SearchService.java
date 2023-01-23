package com.twitter.service;

import com.twitter.database.model.TwitterUserEntity;
import com.twitter.database.repository.TwitterUserRepository;
import com.twitter.dto.controller.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final TwitterUserRepository twitterUserRepository;

    @Autowired
    public SearchService(TwitterUserRepository twitterUserRepository) {
        this.twitterUserRepository = twitterUserRepository;
    }

    public List<Tweet> searchTweets(String name) {
        TwitterUserEntity user = twitterUserRepository.findByName(name).orElse(null);
        if (user != null && user.getTweets() != null) {
            return user.getTweets().stream()
                    .map(tweet -> new Tweet(tweet.getId(), tweet.getCreatedAt(), tweet.getText()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
