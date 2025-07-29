package my.everyconti.every_conti.modules.song.dto.request;

import lombok.*;
import my.everyconti.every_conti.constant.song.SongKey;
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
    private String praiseTeamId;
    private List<String> themeIds;;
    private SongTempo tempo;
    private String seasonId;
    private SongKey songKey;
    private Integer duration;
    private String bibleId;
    private String bibleChapterId;
    private String bibleVerseId;

    private Long offset;
}
