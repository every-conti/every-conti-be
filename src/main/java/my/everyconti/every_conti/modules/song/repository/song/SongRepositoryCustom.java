package my.everyconti.every_conti.modules.song.repository.song;

import my.everyconti.every_conti.modules.song.domain.Song;

public interface SongRepositoryCustom {
    public Song getSongByIdWithJoin(Long innerSongId);

}
