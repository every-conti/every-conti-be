package my.everyconti.every_conti.modules.song.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import my.everyconti.every_conti.constant.song.SongKey;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.bible.domain.Bible;
import my.everyconti.every_conti.modules.bible.domain.BibleChapter;
import my.everyconti.every_conti.modules.bible.domain.BibleVerse;
import my.everyconti.every_conti.modules.conti.domain.ContiSong;
import my.everyconti.every_conti.modules.member.domain.Member;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song extends NowTimeForJpa {
    @JsonIgnore
    @Id
    @Column(name = "song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    --------Main columns--------
     */
    @Column(name = "song_name", nullable = false, length = 100)
    private String songName;

    @Column(name = "lyrics", length = 5000)
    private String lyrics;

    @Column(name = "reference", nullable = false, length = 150)
    private String reference;

    @Column(name = "song_type")
    @Enumerated(EnumType.STRING)
    private SongType songType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "praise_team_id")
    private PraiseTeam praiseTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private Member creator;

    @Column(name = "thumbnail")
    private String thumbnail;

    /*
    --------Sub columns--------
     */

    @Column(name = "tempo")
    @Enumerated(EnumType.STRING)
    private SongTempo tempo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bible_id")
    private Bible bible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bible_chapter_id")
    private BibleChapter bibleChapter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bible_verse_id")
    private BibleVerse bibleVerse;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ContiSong> contis;

    /*
    --------auto columns--------
     */

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // youtube api
    @Column(name = "duration")
    private Integer duration;

    // api
    @Column(name = "key")
    private SongKey key;

    // llm api?
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size= 20)
    private Set<SongSongTheme> songThemes  = new HashSet<>();
}
