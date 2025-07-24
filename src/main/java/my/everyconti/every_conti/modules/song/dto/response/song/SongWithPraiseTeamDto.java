package my.everyconti.every_conti.modules.song.dto.response.song;

import lombok.Getter;
import lombok.Setter;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

@Getter
@Setter
public class SongWithPraiseTeamDto {
    private String id;
    private String songName;
    private String duration;
    private SongType songType;
    private PraiseTeamDto praiseTeam;
    private String thumbnail;

    public SongWithPraiseTeamDto(Song song, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(song.getId());
        songName = song.getSongName();
        duration = song.getDuration();
        songType = song.getSongType();
        praiseTeam = song.getPraiseTeam() == null ? null : new PraiseTeamDto(song.getPraiseTeam(), hashIdUtil);
        thumbnail = song.getThumbnail();
    }
}
