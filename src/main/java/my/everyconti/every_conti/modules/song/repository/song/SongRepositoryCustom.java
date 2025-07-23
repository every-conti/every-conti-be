package my.everyconti.every_conti.modules.song.repository.song;

import my.everyconti.every_conti.modules.song.domain.Song;

import java.util.List;

public interface SongRepositoryCustom {
    public Song getSongByIdWithJoin(Long innerSongId);
    public List<Song> findLastSongsWithPraiseTeam(Integer count);
}
