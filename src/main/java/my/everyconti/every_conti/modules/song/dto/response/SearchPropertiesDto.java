package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;

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
}
