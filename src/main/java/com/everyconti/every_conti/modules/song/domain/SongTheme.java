package com.everyconti.every_conti.modules.song.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@BatchSize(size = 50)
public class SongTheme {
    @Id
    @Column(name = "theme_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theme_name", nullable = false)
    private String themeName;

    @Column(name = "order_index", unique = true)
    private Integer orderIndex;

    @OneToMany(mappedBy = "songTheme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SongSongTheme> songs;
}