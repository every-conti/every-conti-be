package my.everyconti.every_conti.modules.song.repository;

import my.everyconti.every_conti.modules.song.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Override
    Song save(Song song);

    @Override
    Optional<Song> findById(Long id);

//    @EntityGraph
//    Optional<Member> findOneWithRolesByEmail(String email);
}
