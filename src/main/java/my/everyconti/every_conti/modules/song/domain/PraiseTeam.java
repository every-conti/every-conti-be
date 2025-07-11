package my.everyconti.every_conti.modules.song.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PraiseTeam {
    @Id
    @Column(name = "praise_team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "praise_team_name", nullable = false)
    private String praiseTeamName;
}