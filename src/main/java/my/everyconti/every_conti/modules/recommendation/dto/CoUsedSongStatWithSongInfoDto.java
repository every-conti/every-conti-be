package my.everyconti.every_conti.modules.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import my.everyconti.every_conti.modules.song.domain.Song;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoUsedSongStatWithSongInfoDto {
    private SongWithPraiseTeamDto song;
    private Long usageCount;

    public CoUsedSongStatWithSongInfoDto(CoUsedSongStat coUsedSongStat, Song song, HashIdUtil hashIdUtil) {
        this.song = new SongWithPraiseTeamDto(song, hashIdUtil);
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
