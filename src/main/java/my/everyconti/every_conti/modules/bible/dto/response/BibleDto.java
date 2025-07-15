package my.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleDto {
    private String id;
    private String bibleEnName;
    private String bibleKoName;
    private Integer chaptersCount;
    private Integer[] chapterVerse;
    private List<BibleChapterDto> chapters;

    @Override
    public String toString() {
        return "BibleDto{" +
                "id='" + id + '\'' +
                ", bibleEnName='" + bibleEnName + '\'' +
                ", bibleKoName='" + bibleKoName + '\'' +
                ", chaptersCount=" + chaptersCount +
                ", chapters=" + chapterVerse +
                '}';
    }
}
