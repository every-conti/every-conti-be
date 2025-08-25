package com.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.conti.domain.Conti;
import com.everyconti.every_conti.modules.member.dto.MemberSimpleDto;
import com.everyconti.every_conti.modules.song.dto.response.song.SongSimpleDto;

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
    private MemberSimpleDto creator;
    private String description;
    private LocalDateTime createdAt;

    public ContiWithSongDto(Conti conti, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(conti.getId());
        title = conti.getTitle();
        date = conti.getDate();
        songs = conti.getContiSongs() == null ? null : conti.getContiSongs().stream().map(cs -> new SongSimpleDto(cs.getSong(), cs.getOrderIndex(), hashIdUtil)).collect(Collectors.toList());
        creator = new MemberSimpleDto(conti.getCreator(), hashIdUtil);
        createdAt = conti.getCreatedAt();
        description = conti.getDescription();
    }

    @Override
    public String toString() {
        return "ContiDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", songs=" + songs +
                ", creator='" + creator.toString() + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
