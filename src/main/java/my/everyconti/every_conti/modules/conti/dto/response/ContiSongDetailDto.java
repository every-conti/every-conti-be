package my.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.conti.domain.ContiSong;
import my.everyconti.every_conti.modules.song.dto.response.SongDto;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContiSongDetailDto {
    private String id;
    private SongDto song;
    private Integer orderIndex;

    public ContiSongDetailDto(ContiSong contiSong, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(contiSong.getId());
        this.song = new SongDto(contiSong.getSong(), hashIdUtil);
        this.orderIndex = contiSong.getOrderIndex();
    }

    @Override
    public String toString() {
        return "ContiSongDetailDto{" +
                "id='" + id + '\'' +
                ", song=" + song +
                ", orderIndex=" + orderIndex +
                '}';
    }
}
