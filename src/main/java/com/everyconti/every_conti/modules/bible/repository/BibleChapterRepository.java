package com.everyconti.every_conti.modules.bible.repository;

import com.everyconti.every_conti.modules.bible.domain.BibleChapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibleChapterRepository extends JpaRepository<BibleChapter, Long> {

    @Override
    BibleChapter save(BibleChapter bibleChapter);
}
