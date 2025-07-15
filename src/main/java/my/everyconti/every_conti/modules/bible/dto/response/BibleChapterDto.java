package my.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.bible.domain.BibleChapter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleChapterDto {
    private String id;
    private Integer chapterNum;
    private Integer verseCount;

    @Override
    public String toString() {
        return "BibleChapterDto{" +
                "chapterNum=" + chapterNum +
                ", verseCount=" + verseCount +
                '}';
    }

    public BibleChapterDto(BibleChapter bibleChapter, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(bibleChapter.getId());
        this.chapterNum = bibleChapter.getChapterNum();
        this.verseCount = bibleChapter.getVerseCount();
    }
}
