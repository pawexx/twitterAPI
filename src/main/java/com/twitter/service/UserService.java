package com.twitter.service;

import com.twitter.database.model.TwitterUserEntity;
import com.twitter.database.repository.TwitterUserRepository;
import com.twitter.dto.twitter.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@Slf4j
public class UserService {
    private static final String SEARCH_USER_BY_USERNAME_PATH = "users/by/username/";
    private static final String AUTHORIZATION = "Authorization";
    private final TwitterUserRepository twitterUserRepository;
    private final String baseUrl;
    private final String authKey;
    public UserService(@Value("${twitter.api.baseUrl}") String baseUrl,
                       @Value("${twitter.api.authKey}") String authKey,
                       TwitterUserRepository twitterUserRepository) {
        this.baseUrl = baseUrl;
        this.authKey = authKey;
        this.twitterUserRepository = twitterUserRepository;
    }

    public UserData getUserNameFromTwitter(String userName) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder uri = new StringBuilder(baseUrl).append(SEARCH_USER_BY_USERNAME_PATH).append(userName);
        ResponseEntity<UserData> response;
        try {
            response = restTemplate.exchange(
                    uri.toString(), HttpMethod.GET, prepareHttpEntityWithAuthentication(), new ParameterizedTypeReference<UserData>(){});
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error invoking twitter service " + e.getMessage(), e);
            return null;
        }
    }

    protected HttpEntity<String> prepareHttpEntityWithAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(AUTHORIZATION, String.format("Bearer %s", authKey));
        return new HttpEntity<>(headers);
    }

    protected TwitterUserEntity getTwitterUserData(String userName) {
        TwitterUserEntity twitterUser = twitterUserRepository.findByName(userName).orElse(null);
        if (twitterUser == null) {
            UserData user = getUserNameFromTwitter(userName);
            twitterUser = mapTwitterUserToEntity(user.getUser());
            twitterUserRepository.save(twitterUser);
        }
        return twitterUser;
    }

    private TwitterUserEntity mapTwitterUserToEntity(UserData.User user) {
        return new TwitterUserEntity(user.getId(), user.getUserName());
    }

}
