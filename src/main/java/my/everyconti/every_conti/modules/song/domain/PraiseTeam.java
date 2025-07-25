package my.everyconti.every_conti.modules.song.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.modules.conti.domain.Conti;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PraiseTeam {
    @JsonIgnore
    @Id
    @Column(name = "praise_team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "praise_team_name", nullable = false)
    private String praiseTeamName;

    @Column(name = "is_famous")
    private Boolean isFamous;

    @Column(name = "preview_img")
    private String previewImg;
}