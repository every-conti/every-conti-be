package my.everyconti.every_conti.modules.recommendation.dto;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongWithPraiseTeamDto {
    private String id;
    private String songName;
    private SongType songType;
    private PraiseTeamDto praiseTeam;
    private String thumbnail;
    private Integer duration;

    public SongWithPraiseTeamDto(Song song, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(song.getId());
        this.songName =  song.getSongName();
        this.songType = song.getSongType();
        this.praiseTeam = new PraiseTeamDto(song.getPraiseTeam(), hashIdUtil);
        this.thumbnail = song.getThumbnail();
        this.duration = song.getDuration();
    }

    @Override
    public String toString() {
        return "SongWithPraiseTeamDto{" +
                "id='" + id + '\'' +
                ", songName='" + songName + '\'' +
                ", songType=" + songType +
                ", praiseTeam=" + praiseTeam +
                ", thumbnail='" + thumbnail + '\'' +
                ", duration=" + duration +
                '}';
    }
}
