package com.twitter.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
