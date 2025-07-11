package my.everyconti.every_conti.modules.song.dto.response;

import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.modules.song.domain.PraiseTeam;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PraiseTeamDto {
    private Long id;

    private String praiseTeamName;

    public PraiseTeamDto(PraiseTeam praiseTeam) {
        id = praiseTeam.getId();
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