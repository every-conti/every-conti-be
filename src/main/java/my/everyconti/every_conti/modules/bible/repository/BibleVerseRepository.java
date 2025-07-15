package my.everyconti.every_conti.modules.bible.repository;

import my.everyconti.every_conti.modules.bible.domain.BibleVerse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibleVerseRepository extends JpaRepository<BibleVerse, Long> {

    @Override
    BibleVerse save(BibleVerse bibleVerse);
}
