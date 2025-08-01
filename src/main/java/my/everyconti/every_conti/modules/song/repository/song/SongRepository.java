package my.everyconti.every_conti.modules.song.repository.song;

import my.everyconti.every_conti.modules.song.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {

    @Override
    Song save(Song song);

    @Override
    Optional<Song> findById(Long id);

    Song findByYoutubeVId(String youtubeVId);

//    @EntityGraph
//    Optional<Member> findOneWithRolesByEmail(String email);
}
