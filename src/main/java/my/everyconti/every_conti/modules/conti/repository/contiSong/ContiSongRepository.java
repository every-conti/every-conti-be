package my.everyconti.every_conti.modules.conti.repository.contiSong;

import my.everyconti.every_conti.modules.conti.domain.ContiSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ContiSongRepository extends JpaRepository<ContiSong, Long>, ContiSongRepositoryCustom {

    @Override
    ContiSong save(ContiSong contiSong);

    void deleteByContiId(Long contiId);

    @Modifying
    @Query("""
        delete from ContiSong cs 
        where cs.conti.id in (
            select c.id from Conti c where c.creator.id = :memberId
        )
    """)
    int deleteAllByMemberId(@Param("memberId") Long memberId);
}
