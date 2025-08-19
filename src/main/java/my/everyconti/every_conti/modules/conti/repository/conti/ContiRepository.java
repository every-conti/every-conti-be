package my.everyconti.every_conti.modules.conti.repository.conti;

import my.everyconti.every_conti.modules.conti.domain.Conti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContiRepository extends JpaRepository<Conti, Long>, ContiRepositoryCustom {

    @Override
    Conti save(Conti conti);

    Conti findContiById(Long id);

    List<Conti> findContiByCreatorId(Long id);
}
