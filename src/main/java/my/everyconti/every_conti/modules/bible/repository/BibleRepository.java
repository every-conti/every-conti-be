package my.everyconti.every_conti.modules.bible.repository;

import my.everyconti.every_conti.modules.bible.domain.Bible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BibleRepository extends JpaRepository<Bible, Long> {

    @Override
    Bible save(Bible bible);

    @Override
    List<Bible> findAll();
}
