package com.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.conti.domain.Conti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContiSimpleDto {
    private String id;
    private String title;
    private LocalDate date;
    private List<ContiSongSimpleDto> contiSongs;
    private String creatorId;
    private LocalDateTime createdAt;

    public ContiSimpleDto(Conti conti, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(conti.getId());
        this.title = conti.getTitle();
        this.date = conti.getDate();
        this.contiSongs = conti.getContiSongs() == null ? null : conti.getContiSongs().stream().map(s -> new ContiSongSimpleDto(s, hashIdUtil)).collect(Collectors.toList());
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
