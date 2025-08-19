package my.everyconti.every_conti.modules.conti.dto.request;

import lombok.*;
import my.everyconti.every_conti.constant.song.SongType;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchContiDto {
    private Long offset;

    private String text;
    private List<String> songIds;
    private String praiseTeamId;
    private Boolean isFamous;
    private SongType songType;
    private Boolean includePersonalConti;
    private Integer minTotalDuration;
    private Integer maxTotalDuration;
}
