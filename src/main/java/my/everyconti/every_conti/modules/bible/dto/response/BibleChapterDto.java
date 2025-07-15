package my.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleChapterDto {
    private Integer chapterNum;
    private Integer verseCount;
    private List<BibleVerseDto> verses;

    @Override
    public String toString() {
        return "BibleChapterDto{" +
                "chapterNum=" + chapterNum +
                ", verseCount=" + verseCount +
                ", verses=" + verses +
                '}';
    }
}
