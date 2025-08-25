package com.everyconti.every_conti.modules.conti.repository.conti;

import com.everyconti.every_conti.modules.conti.domain.Conti;
import com.everyconti.every_conti.modules.conti.dto.request.SearchContiDto;

import java.util.List;

public interface ContiRepositoryCustom {
    Conti getContiByIdWithJoin(Long innerContiId);
    Conti getContiAndContiSongByContiId(Long innterContId);
    List<Conti> findContisWithJoin();
    Conti getContiDetail(Long innerContiId);
    List<Conti> findLastContiOfFamousPraiseTeams();
    List<Conti> findContisWithSearchParams(SearchContiDto searchContiDto);
    List<Conti> findContisWithMemberId(Long memberId, Long offset);
}
