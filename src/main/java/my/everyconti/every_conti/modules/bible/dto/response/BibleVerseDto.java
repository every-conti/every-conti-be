package my.everyconti.every_conti.modules.bible.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibleVerseDto {
    private Integer verseNum;
    private String content;

    @Override
    public String toString() {
        return "BibleVerseDto{" +
                "verseNum=" + verseNum +
                ", content='" + content + '\'' +
                '}';
    }
}
