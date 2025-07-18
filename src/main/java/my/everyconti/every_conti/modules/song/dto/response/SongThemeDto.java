package my.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.song.domain.SongTheme;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongThemeDto {
    private String id;
    private String themeName;

    public SongThemeDto(SongTheme theme, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(theme.getId());
        themeName = theme.getThemeName();
    }
}

