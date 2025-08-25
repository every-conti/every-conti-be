package com.everyconti.every_conti.modules.conti.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.everyconti.every_conti.common.entity.NowTimeForJpa;
import com.everyconti.every_conti.modules.song.domain.Song;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "conti_song",
        indexes = {
                @Index(name = "idx_conti_song__conti_order", columnList = "conti, order_index"),
                @Index(name = "idx_conti_song__song", columnList = "song") // 선택
        },
        uniqueConstraints = {
                // 선택: 같은 콘티 내 order_index 중복 방지
                @UniqueConstraint(name = "ux_conti_song__conti_order", columnNames = {"conti", "order_index"}),
                // 선택: 같은 콘티 내 동일 곡 중복 방지
                // @UniqueConstraint(name = "ux_conti_song__conti_song", columnNames = {"conti", "song"})
        }
)
public class ContiSong extends NowTimeForJpa {
    @JsonIgnore
    @Id
    @Column(name = "conti_song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song")
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conti")
    private Conti conti;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
