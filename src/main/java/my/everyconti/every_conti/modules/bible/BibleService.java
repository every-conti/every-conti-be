package my.everyconti.every_conti.modules.bible;

import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.bible.domain.*;
import my.everyconti.every_conti.modules.bible.dto.request.CreateBibleDto;
import my.everyconti.every_conti.modules.bible.dto.request.FindBilbeDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleChapterDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleVerseDto;
import my.everyconti.every_conti.modules.bible.repository.BibleChapterRepository;
import my.everyconti.every_conti.modules.bible.repository.BibleRepository;
import my.everyconti.every_conti.modules.bible.repository.BibleVerseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BibleService {

    private final BibleRepository bibleRepository;
    private final HashIdUtil hashIdUtil;
    private final JPAQueryFactory queryFactory;
    private final BibleChapterRepository bibleChapterRepository;
    private final BibleVerseRepository bibleVerseRepository;

    public List<BibleDto> getBibleLists(){
        QBible bible = QBible.bible;
        List<Bible> bibles = queryFactory
                .selectFrom(bible)
                .distinct()
                .fetch();
        return bibles.stream().map(b ->
                BibleDto.builder()
                        .id(hashIdUtil.encode(b.getId()))
                        .bibleKoName(b.getBibleKoName())
                        .bibleEnName(b.getBibleEnName())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<BibleChapterDto> getChapterListsByBibleId(String bibleId){
        QBibleChapter bibleChapter = QBibleChapter.bibleChapter;

        List<BibleChapter> chapters = queryFactory
                .selectFrom(bibleChapter)
                .leftJoin(QBible.bible.chapters).fetchJoin()
                .where(bibleChapter.bible.id.eq(hashIdUtil.decode(bibleId)))
                .distinct()
                .fetch();

        return chapters.stream().map(ch ->
                BibleChapterDto.builder()
                    .id(hashIdUtil.encode(ch.getId()))
                    .chapterNum(ch.getChapterNum())
                    .verseCount(ch.getVerseCount())
                    .build()
        ).collect(Collectors.toList());
    }
    public List<BibleVerseDto> getVerseListsByChapterId(String chapterId){
        QBibleVerse bibleVerse = QBibleVerse.bibleVerse;

        List<BibleVerse> verses = queryFactory
                .selectFrom(bibleVerse)
                .leftJoin(QBible.bible.chapters).fetchJoin()
                .where(bibleVerse.bibleChapter.id.eq(hashIdUtil.decode(chapterId)))
                .distinct()
                .fetch();

        return verses.stream().map(v ->
                BibleVerseDto.builder()
                        .id(hashIdUtil.encode(v.getId()))
                        .verseNum(v.getVerseNum())
                        .build()
        ).collect(Collectors.toList());
    }

    public BibleDto createBible(CreateBibleDto createBibleDto){
        // 1. Bible 저장
        Bible bible = Bible.builder()
                .bibleEnName(createBibleDto.getBibleEnName())
                .bibleKoName(createBibleDto.getBibleKoName())
                .chaptersCount(createBibleDto.getChaptersCount())
                .build();

        bibleRepository.save(bible);

        // 2. 각 chapter 별로 verse 수 저장
        List<BibleChapter> chapters = new ArrayList<>();
        List<BibleVerse> verses = new ArrayList<>();
        for (int i = 0; i < createBibleDto.getVerseList().size(); i++) {
            Integer verseCount = createBibleDto.getVerseList().get(i);

            BibleChapter chapter = BibleChapter.builder()
                    .bible(bible)
                    .chapterNum(i + 1)
                    .verseCount(verseCount)
                    .build();

            chapters.add(chapter);

            for (int j = 0; j < createBibleDto.getVerseList().get(i); j++){
                BibleVerse verse = BibleVerse.builder()
                        .bibleChapter(chapter)
                        .verseNum(j+1)
                        .content("")
                        .build();
                verses.add(verse);
            }
        }

        bibleChapterRepository.saveAll(chapters);
        bibleVerseRepository.saveAll(verses);

        // 3. DTO로 변환
        List<BibleChapterDto> chapterDtos = chapters.stream()
                .map(c -> BibleChapterDto.builder()
                        .chapterNum(c.getChapterNum())
                        .verseCount(c.getVerseCount())
                        .build())
                .toList();

        return BibleDto.builder()
                .id(hashIdUtil.encode(bible.getId()))
                .bibleEnName(bible.getBibleEnName())
                .bibleKoName(bible.getBibleKoName())
                .chaptersCount(bible.getChaptersCount())
                .build();
    }

    public Bible findBible(FindBilbeDto dto){
        return bibleRepository.findById(hashIdUtil.decode(dto.getBibleId())).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성경입니다"));
    }
}
