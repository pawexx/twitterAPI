package com.twitter.dto.twitter;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TweetData {
    @SerializedName("data")
    private List<Tweet> tweets;
    @SerializedName("meta")
    private MetaData metaData;

    @Getter
    @Setter
    public class Tweet {
        private Long id;
        @SerializedName("created_at")
        private LocalDateTime createdAt;
        private String text;
    }
    @Getter
    @Setter
    public class MetaData {
        @SerializedName("next_token")
        private String nextToken;
    }

}
