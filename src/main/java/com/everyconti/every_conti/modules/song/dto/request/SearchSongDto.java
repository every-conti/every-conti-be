package com.everyconti.every_conti.modules.song.dto.request;

import lombok.*;
import com.everyconti.every_conti.constant.song.SongKey;
import com.everyconti.every_conti.constant.song.SongTempo;
import com.everyconti.every_conti.constant.song.SongType;

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
    private Integer minDuration;
    private Integer maxDuration;
    private String bibleId;
    private String bibleChapterId;
    private String bibleVerseId;

    private Long offset;
}
