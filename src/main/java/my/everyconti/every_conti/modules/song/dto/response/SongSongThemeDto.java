package my.everyconti.every_conti.modules.song.dto.response;

import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.modules.member.domain.MemberRole;
import my.everyconti.every_conti.modules.song.domain.SongSongTheme;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSongThemeDto {
    private Long id;
    private String songThemeName;

    public SongSongThemeDto(SongSongTheme theme) {
        id = theme.getId();
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