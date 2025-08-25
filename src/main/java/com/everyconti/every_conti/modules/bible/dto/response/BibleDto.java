package com.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.bible.domain.Bible;

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

    @Override
    public String toString() {
        return "BibleDto{" +
                "id='" + id + '\'' +
                ", bibleEnName='" + bibleEnName + '\'' +
                ", bibleKoName='" + bibleKoName + '\'' +
                ", chaptersCount=" + chaptersCount +
                '}';
    }

    public BibleDto(Bible bible, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(bible.getId());
        this.bibleEnName = bible.getBibleEnName();
        this.bibleKoName = bible.getBibleKoName();
        this.chaptersCount = bible.getChaptersCount();
    }
}
