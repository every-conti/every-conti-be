package my.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.bible.domain.BibleChapter;
import my.everyconti.every_conti.modules.bible.domain.BibleVerse;

import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleVerseDto {
    private String id;
    private Integer verseNum;
    private String content;

    @Override
    public String toString() {
        return "BibleVerseDto{" +
                "verseNum=" + verseNum +
                ", content='" + content + '\'' +
                '}';
    }

    public BibleVerseDto(BibleVerse bibleVerse, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(bibleVerse.getId());
        this.verseNum = bibleVerse.getVerseNum();
        this.content = bibleVerse.getContent();
    }
}
