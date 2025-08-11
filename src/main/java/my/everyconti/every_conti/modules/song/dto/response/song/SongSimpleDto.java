package my.everyconti.every_conti.modules.song.dto.response.song;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.song.domain.Song;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSimpleDto {
    private MinimumSongToPlayDto song;
    private Integer idx;

    public SongSimpleDto(Song song, Integer idx, HashIdUtil hashIdUtil) {
        this.song = new MinimumSongToPlayDto(song, hashIdUtil);
        this.idx = idx;
    }

    @Override
    public String toString() {
        return "SongSimpleDto{" +
                "song=" + song +
                ", idx=" + idx +
                '}';
    }
}
