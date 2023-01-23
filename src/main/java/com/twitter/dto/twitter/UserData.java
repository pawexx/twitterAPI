package com.twitter.dto.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
    @JsonProperty("data")
    private User user;

    @Getter
    @Setter
    public class User {
        private Long id;
        @JsonProperty("username")
        private String userName;
    }
}
