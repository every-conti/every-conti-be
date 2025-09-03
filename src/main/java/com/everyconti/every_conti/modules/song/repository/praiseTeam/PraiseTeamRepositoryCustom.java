package com.everyconti.every_conti.modules.song.repository.praiseTeam;

import com.everyconti.every_conti.modules.song.domain.PraiseTeam;

import java.util.List;

public interface PraiseTeamRepositoryCustom {
    List<PraiseTeam> findSearchablePraiseTeams();
}
