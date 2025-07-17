package my.everyconti.every_conti.modules.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoUsedSongStatDto {

    private String id;
    private Long songIdA;
    private Long songIdB;
    private Long usageCount = 0L;

    public CoUsedSongStatDto(CoUsedSongStat coUsedSongStat, HashIdUtil hashId) {
        this.id = hashId.encode(coUsedSongStat.getId());
        this.songIdA = coUsedSongStat.getSongIdA();
        this.songIdB = coUsedSongStat.getSongIdB();
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
