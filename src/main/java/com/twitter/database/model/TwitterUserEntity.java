package com.twitter.database.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TwitterUserEntity {
    @Id
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "twitterUser", orphanRemoval = true)
    @BatchSize(size = 500)
    private List<TweetEntity> tweets;
}
