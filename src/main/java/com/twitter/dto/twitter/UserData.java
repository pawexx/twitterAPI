package com.twitter.dto.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserData {
    @JsonProperty("data")
    private User user;

    @Getter
    public class User {
        private Long id;
        @JsonProperty("username")
        private String userName;
    }
}
