package com.everyconti.every_conti.modules.song.repository;

import com.everyconti.every_conti.modules.song.domain.SongTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongThemeRepository extends JpaRepository<SongTheme, Long> {

    @Override
    SongTheme save(SongTheme song);

    @Override
    Optional<SongTheme> findById(Long id);

    @Override
    List<SongTheme> findAllById(Iterable<Long> longs);

    @Override
    List<SongTheme> findAll();

    //    @EntityGraph
//    Optional<Member> findOneWithRolesByEmail(String email);
}
