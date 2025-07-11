package my.everyconti.every_conti.modules.song.dto.request;

import lombok.*;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchSongDto {
    private String text;
    private SongType songType;
//    private Integer creatorId;
    private Integer praiseTeamId;
    private List<Integer> themeIds;;
    private SongTempo tempo;
    private Integer seasonId;
    private String bibleBook;
    private Integer bibleChapter;
    private Integer bibleVerse;

    private Integer offset;
}
