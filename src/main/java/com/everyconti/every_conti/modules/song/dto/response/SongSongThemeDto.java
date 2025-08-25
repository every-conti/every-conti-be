package com.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.song.domain.SongSongTheme;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSongThemeDto {
    private String id;
    private String songThemeName;

    public SongSongThemeDto(SongSongTheme theme, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(theme.getSongTheme().getId());
        songThemeName = theme.getSongTheme().getThemeName();
    }

    @Override
    public String toString() {
        return "SongSongThemeDto{" +
                "id=" + id +
                ", songThemeName='" + songThemeName + '\'' +
                '}';
    }
}