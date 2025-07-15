package my.everyconti.every_conti.modules.bible.domain;

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
public class Bible {
    @JsonIgnore
    @Id
    @Column(name = "bible_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bible_en_name")
    private String bibleEnName;

    @Column(name = "bible_ko_name")
    private String bibleKoName;

    @Column(name = "chapter_count")
    private Integer chaptersCount;

    @OneToMany(mappedBy = "bible", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BibleChapter> chapters;
}
