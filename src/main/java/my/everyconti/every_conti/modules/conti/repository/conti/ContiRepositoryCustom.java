package my.everyconti.every_conti.modules.conti.repository.conti;

import my.everyconti.every_conti.modules.conti.domain.Conti;

import java.time.LocalDateTime;
import java.util.List;

public interface ContiRepositoryCustom {
    Conti getContiByIdWithJoin(Long innerContiId);
    Conti getContiAndContiSongByContiId(Long innterContid);
    List<Conti> findContisWithJoin();
}
