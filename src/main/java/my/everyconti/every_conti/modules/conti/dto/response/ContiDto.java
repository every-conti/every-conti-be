package my.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.song.dto.response.SongDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContiDto {
    private String id;
    private String title;
    private LocalDate date;
    private List<ContiSongDto> contiSongs;
    private String creatorId;
    private LocalDateTime createdAt;

    public ContiDto(Conti conti, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(conti.getId());
        this.title = conti.getTitle();
        this.date = conti.getDate();
        this.contiSongs = conti.getContiSongs() == null ? null : conti.getContiSongs().stream().map(s -> new ContiSongDto(s, hashIdUtil)).collect(Collectors.toList());
        this.creatorId = hashIdUtil.encode(conti.getCreator().getId());
        this.createdAt = conti.getCreatedAt();
    }

    @Override
    public String toString() {
        return "ContiDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", contiSongs=" + contiSongs +
                ", creatorId='" + creatorId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
