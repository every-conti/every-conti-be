package com.everyconti.every_conti.modules.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoUsedSongStatDto {

    private String id;
    private String songIdA;
    private String songIdB;
    private Long usageCount = 0L;

    public CoUsedSongStatDto(CoUsedSongStat coUsedSongStat, HashIdUtil hashId) {
        this.id = hashId.encode(coUsedSongStat.getId());
        this.songIdA = hashId.encode(coUsedSongStat.getSongIdA());
        this.songIdB = hashId.encode(coUsedSongStat.getSongIdB());
        this.usageCount = coUsedSongStat.getUsageCount();
    }

    @Override
    public String toString() {
        return "CoUsedSongStatDto{" +
                "id='" + id + '\'' +
                ", songIdA=" + songIdA +
                ", songIdB=" + songIdB +
                ", usageCount=" + usageCount +
                '}';
    }
}
