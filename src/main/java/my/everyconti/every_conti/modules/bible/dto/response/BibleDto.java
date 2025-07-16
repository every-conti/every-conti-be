package my.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.bible.domain.Bible;
import my.everyconti.every_conti.modules.member.dto.MemberRoleDto;

import java.util.List;
import java.util.stream.Collectors;

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
