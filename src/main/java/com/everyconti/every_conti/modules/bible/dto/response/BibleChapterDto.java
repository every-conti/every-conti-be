package com.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.bible.domain.BibleChapter;

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
