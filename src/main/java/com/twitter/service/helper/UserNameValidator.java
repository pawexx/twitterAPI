package com.twitter.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserNameValidator {
    private final List<String> validUserNames;

    @Autowired
    public UserNameValidator(@Value("${twitter.valid.usernames}") List<String> validUserNames) {
        this.validUserNames = validUserNames;
    }

    public boolean validUserName(String userName) {
        return validUserNames.stream().anyMatch(name -> name.equals(userName));
    }
}
