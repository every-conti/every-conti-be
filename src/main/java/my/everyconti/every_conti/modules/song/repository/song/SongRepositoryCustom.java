package my.everyconti.every_conti.modules.song.repository.song;

import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;

import java.util.List;

public interface SongRepositoryCustom {
    Song findSongByIdWithJoin(Long innerSongId);
    Song findSongByIdWithJoinAll(Long innerSongId);
    List<Song> findLastSongsWithPraiseTeam(Integer count);
    List<Song> findSongsWithSearchParams(SearchSongDto searchSongDto, HashIdUtil hashIdUtil);
}
