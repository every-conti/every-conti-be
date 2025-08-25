package com.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.song.domain.Season;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDto {
    private String id;

    private String seasonName;

    private LocalDate startDate;

    private LocalDate endDate;

    public SeasonDto(Season season, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(season.getId());
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