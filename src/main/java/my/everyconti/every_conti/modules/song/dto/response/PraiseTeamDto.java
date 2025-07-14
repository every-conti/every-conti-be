package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.song.domain.PraiseTeam;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PraiseTeamDto {
    private String id;

    private String praiseTeamName;

    public PraiseTeamDto(PraiseTeam praiseTeam, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(praiseTeam.getId());
        praiseTeamName = praiseTeam.getPraiseTeamName();
    }

    @Override
    public String toString(){
        return "PraiseTeamDto{" +
                "id='" + getId() + '\'' +
                "praiseTeamName='" + getPraiseTeamName() + '\'' +
                '}';
    }
}