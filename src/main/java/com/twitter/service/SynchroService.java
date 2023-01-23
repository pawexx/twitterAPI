package com.twitter.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.twitter.database.model.TweetEntity;
import com.twitter.database.model.TwitterUserEntity;
import com.twitter.database.repository.TweetRepository;
import com.twitter.dto.twitter.TweetData;
import com.twitter.service.helper.LocalDateTimeAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableScheduling
public class SynchroService {
    private final String baseUrl;
    private final TweetRepository tweetRepository;
    private final RestTemplate restTemplate;
    private final UserService userService;
    //The dateTime for first time use, when no tweets in database will be found
    private final String dateTimeFrom = "2023-01-01T00:00:00Z";
    private final List<String> validUserNames;
    private final int maxTweetsInRequest;

    public SynchroService(@Value("${twitter.api.baseUrl}") String baseUrl,
                          @Value("${twitter.valid.userNames}") List<String> validUserNames,
                          @Value("${twitter.api.maxTweetsAmountInRequest}") int maxTweetsInRequest,
                          TweetRepository tweetRepository,
                          UserService userService) {
        this.baseUrl = baseUrl;
        this.tweetRepository = tweetRepository;
        this.maxTweetsInRequest = maxTweetsInRequest;
        this.restTemplate = new RestTemplate();
        this.validUserNames = validUserNames;
        this.userService = userService;
    }

    @Scheduled(fixedDelay =1*60*1000)
    public void synchronizeNewTweets() {
        for (String userName : validUserNames) {
            TwitterUserEntity user = userService.getTwitterUserData(userName);
            Optional<LocalDateTime> lastTweetDate = tweetRepository.findLatestTweetDate(user.getId());
            TweetData tweetsData;
            String startDateTime = lastTweetDate.isPresent() ? formatDateForTwitter(lastTweetDate.get().plusSeconds(1L)) : dateTimeFrom;
            tweetsData = getTweets(user.getId(), startDateTime, "");
            if(tweetsData.getTweets() != null) {
                saveTweets(user, tweetsData.getTweets());
            }
            while (tweetsData.getMetaData() != null && StringUtils.hasLength(tweetsData.getMetaData().getNextToken())) {
                tweetsData = getTweets(user.getId(), startDateTime, tweetsData.getMetaData().getNextToken());
                if(tweetsData.getTweets() != null) {
                    saveTweets(user, tweetsData.getTweets());
                }
            }
        }
    }

    private TweetData getTweets(Long userId, String searchTweetFromDate, String nextToken) {
        StringBuilder uri = new StringBuilder(baseUrl).append("users/").append(userId)
                .append("/tweets?tweet.fields=created_at&max_results=").append(maxTweetsInRequest).append("&start_time=")
                .append(searchTweetFromDate).append(!nextToken.isEmpty() ? "&pagination_token=" + nextToken : "");
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uri.toString(), HttpMethod.GET, userService.prepareHttpEntityWithAuthentication(), new ParameterizedTypeReference<String>(){});
            return mapDataFromJson(response.getBody());
        } catch (RestClientException e) {
            log.error("Error invoking twitter service " + e.getMessage(), e);
            return new TweetData();
        }
    }

    private void saveTweets(TwitterUserEntity user, List<TweetData.Tweet> tweets) {
        List<TweetEntity> tweetsToSave = tweets.stream()
                .map(tmpTweet -> mapTweetDataToEntity(tmpTweet, user))
                .collect(Collectors.toList());
        tweetRepository.saveAll(tweetsToSave);
    }

    private String formatDateForTwitter(LocalDateTime date) {
        return new StringBuilder(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date)).append("Z").toString();
    }

    private TweetEntity mapTweetDataToEntity(TweetData.Tweet tweet, TwitterUserEntity twitterUserEntity) {
        return TweetEntity.builder()
                .id(tweet.getId())
                .text(tweet.getText())
                .createdAt(tweet.getCreatedAt())
                .twitterUser(twitterUserEntity)
                .build();
    }

    private TweetData mapDataFromJson(String json) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        TweetData tweet = new TweetData();
        try {
            tweet = gson.fromJson(json, TweetData.class);
        } catch (JsonSyntaxException ex) {
            log.error("Error during mapping from JSON to GSON " + ex.getMessage(), ex);
            return tweet;
        }
        return tweet;
    }
}
