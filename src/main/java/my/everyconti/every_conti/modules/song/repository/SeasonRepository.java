package my.everyconti.every_conti.modules.song.repository;

import my.everyconti.every_conti.modules.song.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    @Override
    Season save(Season season);

    @Override
    Optional<Season> findById(Long id);

//    @EntityGraph
//    Optional<Member> findOneWithRolesByEmail(String email);
}
