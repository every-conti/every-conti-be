package my.everyconti.every_conti.modules.conti.repository.conti;

import my.everyconti.every_conti.modules.conti.domain.Conti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContiRepository extends JpaRepository<Conti, Long>, ContiRepositoryCustom {

    @Override
    Conti save(Conti conti);

    Conti findContiById(Long id);

    @Modifying
    @Query("delete from Conti c where c.creator.id = :memberId")
    int deleteAllByMemberId(@Param("memberId") Long memberId);
}
