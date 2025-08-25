package com.everyconti.every_conti.modules.bible.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleChapter {
    @JsonIgnore
    @Id
    @Column(name = "bible_chapter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chapter_num")
    private Integer chapterNum;

    @Column(name = "verse_count")
    private Integer verseCount;

    @ManyToOne
    private Bible bible;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BibleVerse> verses;
}
