package my.everyconti.every_conti.modules.song.dto.response.song;

import lombok.Getter;
import lombok.Setter;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongKey;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

@Getter
@Setter
public class MinimumSongToPlayDto {
    private String id;
    private String songName;
    private String youtubeVId;
    private SongType songType;
    private PraiseTeamDto praiseTeam;
    private String thumbnail;
    private SongKey songKey;
    private Integer duration;

    public MinimumSongToPlayDto(Song song, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(song.getId());
        songName = song.getSongName();
        youtubeVId = song.getYoutubeVId();
        duration = song.getDuration();
        songType = song.getSongType();
        praiseTeam = song.getPraiseTeam() == null ? null : new PraiseTeamDto(song.getPraiseTeam(), hashIdUtil);
        thumbnail = song.getThumbnail();
        songKey = song.getKey();
    }
}
