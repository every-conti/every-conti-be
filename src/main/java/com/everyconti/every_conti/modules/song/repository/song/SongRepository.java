package com.everyconti.every_conti.modules.song.repository.song;

import com.everyconti.every_conti.modules.song.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {

    @Override
    Song save(Song song);

    @Override
    Optional<Song> findById(Long id);

    Song findByYoutubeVId(String youtubeVId);

    List<Song> findByIdIn(Collection<Long> ids);

    @Modifying(clearAutomatically = false, flushAutomatically = false)
    @Query("update Song s set s.creator = null where s.creator.id = :memberId")
    int detachMemberFromSongs(@Param("memberId") Long memberId);
}
