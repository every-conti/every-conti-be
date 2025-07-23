package my.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PraiseTeamContiDto {
    private PraiseTeamDto praiseTeam;
    private ContiWithSongDto conti;
}
