package my.everyconti.every_conti.modules.conti.repository.conti;

import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.song.domain.PraiseTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ContiRepository extends JpaRepository<Conti, Long>, ContiRepositoryCustom {

    @Override
    Conti save(Conti conti);

    List<Conti> findContisByPraiseTeam_Id(Long praiseTeamId);
}
