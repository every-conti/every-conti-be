package my.everyconti.every_conti.modules.conti.repository;

import my.everyconti.every_conti.modules.conti.domain.ContiSong;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ContiSongRepository extends JpaRepository<ContiSong, Long> {

    @Override
    ContiSong save(ContiSong contiSong);
}
