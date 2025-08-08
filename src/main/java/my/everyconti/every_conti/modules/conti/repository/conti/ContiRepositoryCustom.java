package my.everyconti.every_conti.modules.conti.repository.conti;

import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.conti.dto.request.SearchContiDto;

import java.util.List;

public interface ContiRepositoryCustom {
    Conti getContiByIdWithJoin(Long innerContiId);
    Conti getContiAndContiSongByContiId(Long innterContId);
    List<Conti> findContisWithJoin();
    Conti getContiDetail(Long innerContiId);
    List<Conti> findContisByPraiseTeam_Id(Long praiseTeamId);
    List<Conti> findLastContiOfFamousPraiseTeams();
    List<Conti> findSongsWithSearchParams(SearchContiDto searchContiDto);
}
