package com.everyconti.every_conti.modules.song.repository.praiseTeam;

import com.everyconti.every_conti.constant.ResponseMessage;
import com.everyconti.every_conti.modules.song.domain.PraiseTeam;
import com.everyconti.every_conti.modules.song.domain.QPraiseTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PraiseTeamRepositoryImpl implements PraiseTeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PraiseTeam> findSearchablePraiseTeams(){
        QPraiseTeam praiseTeam = QPraiseTeam.praiseTeam;

        List<PraiseTeam> praiseTeams = queryFactory.selectFrom(praiseTeam)
                .where(praiseTeam.searchable.eq(true))
                .orderBy(praiseTeam.id.asc())
                .fetch();

        return praiseTeams;
    }
}
