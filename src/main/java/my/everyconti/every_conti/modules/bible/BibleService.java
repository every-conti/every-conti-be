package my.everyconti.every_conti.modules.bible;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.bible.domain.Bible;
import my.everyconti.every_conti.modules.bible.domain.BibleChapter;
import my.everyconti.every_conti.modules.bible.domain.QBible;
import my.everyconti.every_conti.modules.bible.dto.request.CreateBibleDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleChapterDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleDto;
import my.everyconti.every_conti.modules.bible.repository.BibleChapterRepository;
import my.everyconti.every_conti.modules.bible.repository.BibleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BibleService {

    private final BibleRepository bibleRepository;
    private final HashIdUtil hashIdUtil;
    private final JPAQueryFactory queryFactory;
    private final BibleChapterRepository bibleChapterRepository;

    public List<BibleDto> getBibles(){
        QBible bible = QBible.bible;

        List<Bible> bibles = queryFactory
                .selectFrom(bible)
                .leftJoin(bible.chapters).fetchJoin()
                .distinct()
                .fetch();


        List<BibleDto> result = bibles.stream().map(b -> {
            Integer[] chapterVerse = new Integer[b.getChaptersCount()];
            for (BibleChapter ch: b.getChapters()){
                chapterVerse[ch.getChapterNum() - 1] = ch.getVerseCount();
            }
            return BibleDto.builder()
                    .id(hashIdUtil.encode(b.getId()))
                    .bibleEnName(b.getBibleEnName())
                    .bibleKoName(b.getBibleKoName())
                    .chaptersCount(b.getChaptersCount())
                    .chapterVerse(chapterVerse)
                    .build();
        }).toList();

        return result;
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
        for (int i = 0; i < createBibleDto.getVerseList().size(); i++) {
            Integer verseCount = createBibleDto.getVerseList().get(i);

            BibleChapter chapter = BibleChapter.builder()
                    .bible(bible)
                    .chapterNum(i + 1)
                    .verseCount(verseCount)
                    .build();

            chapters.add(chapter);
        }

        bibleChapterRepository.saveAll(chapters); // 연관관계 저장

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
                .chapters(chapterDtos)
                .build();
    }
}
