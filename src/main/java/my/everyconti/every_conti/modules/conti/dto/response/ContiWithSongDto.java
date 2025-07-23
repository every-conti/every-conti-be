package my.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.song.dto.response.SongSimpleDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContiWithSongDto {
    private String id;
    private String title;
    private LocalDate date;
    private List<SongSimpleDto> songs;
    private String creatorId;
    private LocalDateTime createdAt;

    public ContiWithSongDto(Conti conti, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(conti.getId());
        this.title = conti.getTitle();
        this.date = conti.getDate();
        this.songs = conti.getContiSongs() == null ? null : conti.getContiSongs().stream().map(cs -> new SongSimpleDto(cs.getSong(), cs.getOrderIndex(), hashIdUtil)).collect(Collectors.toList());
        this.creatorId = hashIdUtil.encode(conti.getCreator().getId());
        this.createdAt = conti.getCreatedAt();
    }

    @Override
    public String toString() {
        return "ContiDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", songs=" + songs +
                ", creatorId='" + creatorId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
