package com.everyconti.every_conti.modules.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import com.everyconti.every_conti.modules.song.domain.Song;
import com.everyconti.every_conti.modules.song.dto.response.song.MinimumSongToPlayDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoUsedSongStatWithSongInfoDto {
    private MinimumSongToPlayDto song;
    private Long usageCount;

    public CoUsedSongStatWithSongInfoDto(CoUsedSongStat coUsedSongStat, Song song, HashIdUtil hashIdUtil) {
        this.song = new MinimumSongToPlayDto(song, hashIdUtil);
        this.usageCount = coUsedSongStat.getUsageCount();
    }

    @Override
    public String toString() {
        return "CoUsedSongStatWithSongInfoDto{" +
                "song=" + song +
                ", usageCount=" + usageCount +
                '}';
    }
}
