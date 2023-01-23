package com.twitter.dto.twitter;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TweetData {
    @SerializedName("data")
    private List<Tweet> tweets;
    @SerializedName("meta")
    private MetaData metaData;

    @Getter
    public class Tweet {
        private Long id;
        @SerializedName("created_at")
        private LocalDateTime createdAt;
        private String text;
    }
    @Getter
    public class MetaData {
        @SerializedName("next_token")
        private String nextToken;
    }

}
