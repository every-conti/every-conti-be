package my.everyconti.every_conti.modules.member.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.domain.QMember;
import my.everyconti.every_conti.modules.song.domain.QSong;
import my.everyconti.every_conti.modules.song.domain.QSongSongTheme;
import my.everyconti.every_conti.modules.song.domain.Song;

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
