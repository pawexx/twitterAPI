package com.twitter.database.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class TwitterUserEntity {
    public TwitterUserEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Id
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "twitterUser", orphanRemoval = true)
    @BatchSize(size = 500)
    private List<TweetEntity> tweets;
}
