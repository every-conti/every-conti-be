package my.everyconti.every_conti.modules.conti.dto.response;

import lombok.*;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContiPropertiesDto {
    private List<PraiseTeamDto> praiseTeams;
}
