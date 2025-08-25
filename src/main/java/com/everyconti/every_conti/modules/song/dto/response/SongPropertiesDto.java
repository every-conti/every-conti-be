package com.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import com.everyconti.every_conti.constant.song.SongKey;
import com.everyconti.every_conti.constant.song.SongTempo;
import com.everyconti.every_conti.constant.song.SongType;
import com.everyconti.every_conti.modules.bible.dto.response.BibleDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongPropertiesDto {
    private List<PraiseTeamDto> praiseTeams;
    private List<SeasonDto> seasons;
    private List<SongThemeDto> songThemes;
    private List<SongType> songTypes;
    private List<SongTempo> songTempos;
    private List<BibleDto> bibles;
    private List<SongKey> songKeys;
}
