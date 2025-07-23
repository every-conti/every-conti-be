package my.everyconti.every_conti.modules.song.dto.response.song;

import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;


public class SongWithPraiseTeamDto {
    private String id;
    private String songName;
    private Integer duration;
    private SongType songType;
    private PraiseTeamDto praiseTeam;

    public SongWithPraiseTeamDto(Song song, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(song.getId());
        songName = song.getSongName();
        duration = song.getDuration();
        songType = song.getSongType();
        praiseTeam = song.getPraiseTeam() == null ? null : new PraiseTeamDto(song.getPraiseTeam(), hashIdUtil);
    }
}
