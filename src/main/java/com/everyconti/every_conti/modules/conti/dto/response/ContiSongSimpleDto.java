package com.everyconti.every_conti.modules.conti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.conti.domain.ContiSong;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContiSongSimpleDto {
    private String id;
    private String songId;
    private Integer orderIndex;

    public ContiSongSimpleDto(ContiSong contiSong, HashIdUtil hashIdUtil) {
        this.id = hashIdUtil.encode(contiSong.getId());
        this.songId = hashIdUtil.encode(contiSong.getSong().getId());
        this.orderIndex = contiSong.getOrderIndex();
    }

    @Override
    public String toString() {
        return "ContiSongDto{" +
                "id='" + id + '\'' +
                ", songId=" + songId +
                ", orderIndex=" + orderIndex +
                '}';
    }
}
