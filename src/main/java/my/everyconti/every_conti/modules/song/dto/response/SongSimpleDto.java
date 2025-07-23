package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.song.SongKey;
import my.everyconti.every_conti.modules.song.domain.Song;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSimpleDto {
    private String id;
    private String songName;
    private SongKey songKey;
    private Integer idx;

    public SongSimpleDto(Song song, Integer idx, HashIdUtil hashIdUtil) {
        id= hashIdUtil.encode(song.getId());
        songName = song.getSongName();
        songKey = song.getKey();
        this.idx = idx;
    }

    @Override
    public String toString() {
        return "SongDto{" +
                "id='" + id + '\'' +
                "songName='" + songName + '\'' +
                "songKey='" + songKey + '\'' +
                "idx='" + idx + '}';
    }
}
