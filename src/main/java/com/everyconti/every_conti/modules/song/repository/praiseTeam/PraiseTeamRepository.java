package com.everyconti.every_conti.modules.song.repository.praiseTeam;

import com.everyconti.every_conti.modules.song.domain.PraiseTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PraiseTeamRepository extends JpaRepository<PraiseTeam, Long>, PraiseTeamRepositoryCustom {

    @Override
    PraiseTeam save(PraiseTeam song);

    @Override
    Optional<PraiseTeam> findById(Long id);

    List<PraiseTeam> findPraiseTeamsByIsFamousTrue();
    //    @EntityGraph
//    Optional<Member> findOneWithRolesByEmail(String email);
}
