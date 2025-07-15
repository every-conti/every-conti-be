package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.bible.dto.response.BibleDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPropertiesDto {
    private List<PraiseTeamDto> praiseTeams;
    private List<SeasonDto> seasons;
    private List<SongThemeDto> songThemes;
    private List<SongType> songTypes;
    private List<SongTempo> songTempos;
    private List<BibleDto> bibles;
}
