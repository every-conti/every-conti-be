package my.everyconti.every_conti.modules.bible.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.modules.member.domain.Member;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleVerse {
    @JsonIgnore
    @Id
    @Column(name = "bible_verse_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "verse_num")
    private Integer verseNum;

    @ManyToOne
    @JoinColumn(name = "bible_chapter")
    private BibleChapter bibleChapter;

    @Column(name = "content")
    private String content;

    @ManyToOne
    private BibleChapter chapter;
}
