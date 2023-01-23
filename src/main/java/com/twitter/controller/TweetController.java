package com.twitter.controller;

import com.twitter.dto.controller.Tweet;
import com.twitter.service.SearchService;
import com.twitter.service.helper.UserNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitter")
public class TweetController {
    private final SearchService searchService;
    private final UserNameValidator userNameValidator;

    @Autowired
    public TweetController(SearchService searchService, UserNameValidator userNameValidator) {
        this.searchService = searchService;
        this.userNameValidator = userNameValidator;
    }

    @GetMapping(value = "/{userName}/tweets", headers = "accept=application/json")
    @ResponseBody
    public ResponseEntity<List<Tweet>> getUserTweets(@PathVariable(name = "userName", required = false) String userName) {
        if (userNameValidator.validUserName(userName)) {
            return new ResponseEntity(searchService.searchTweets(userName), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
