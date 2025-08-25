package com.everyconti.every_conti.modules.member.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import com.everyconti.every_conti.modules.member.domain.Member;
import com.everyconti.every_conti.modules.member.domain.QMember;

import java.util.Optional;

@AllArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Optional<Member> findOneWithRolesByEmail(String email){
        QMember member = QMember.member;
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .leftJoin(member.memberRoles).fetchJoin()
                .where(member.email.eq(email))
                .distinct()
                .fetchOne());
    }
}
