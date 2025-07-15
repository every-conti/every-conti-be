package my.everyconti.every_conti.modules.song.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.bible.domain.Bible;
import my.everyconti.every_conti.modules.bible.domain.BibleChapter;
import my.everyconti.every_conti.modules.bible.domain.BibleVerse;
import my.everyconti.every_conti.modules.member.domain.Member;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "praise_team_id")
    private PraiseTeam praiseTeam;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "creator")
    private Member creator;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SongSongTheme> songThemes;

    /*
    --------Sub columns--------
     */

    @Column(name = "tempo")
    @Enumerated(EnumType.STRING)
    private SongTempo tempo;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne
    @JoinColumn(name = "bible_id")
    private Bible bible;

    @ManyToOne
    @JoinColumn(name = "bible_chapter_id")
    private BibleChapter bibleChapter;

    @ManyToOne
    @JoinColumn(name = "bible_verse_id")
    private BibleVerse bibleVerse;
}
