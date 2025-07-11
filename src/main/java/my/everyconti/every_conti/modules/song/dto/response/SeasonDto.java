package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.modules.song.domain.Season;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDto {
    private Long id;

    private String seasonName;

    private LocalDate startDate;

    private LocalDate endDate;

    public SeasonDto(Season season) {
        id = season.getId();
        seasonName = season.getSeasonName();
        startDate = season.getStartDate();
        endDate = season.getEndDate();
    }

    @Override
    public String toString() {
        return "SeasonDto{" +
                "id=" + id +
                ", seasonName='" + seasonName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}