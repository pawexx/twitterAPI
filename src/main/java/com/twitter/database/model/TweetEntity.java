package com.twitter.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@EqualsAndHashCode
public class TweetEntity {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private TwitterUserEntity twitterUser;
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TEXT")
    private String text;
}
