package com.everyconti.every_conti.bible;

import jakarta.transaction.Transactional;
import com.everyconti.every_conti.modules.bible.BibleService;
import com.everyconti.every_conti.modules.bible.dto.request.CreateBibleDto;
import com.everyconti.every_conti.modules.bible.dto.response.BibleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

@SpringBootTest
@Transactional
public class bibleServiceTest {

    @Autowired
    private BibleService bibleService;

    @Test
    @Commit
    public void createBibleTest(){
        CreateBibleDto createBibleDto = new CreateBibleDto();
        createBibleDto.setBibleEnName("Genesis");
        createBibleDto.setBibleKoName("창세기");
        createBibleDto.setChaptersCount(50);
        List<Integer> verseList = List.of(31, 25, 24, 26, 32, 22, 24, 22, 29, 32, 32, 20, 18, 24, 21, 16, 27, 33, 38, 18, 34, 24, 20, 67, 34, 35, 46, 22, 35, 43, 55, 32, 20, 31, 29, 43, 36, 30, 23, 23, 57, 38, 34, 34, 28, 34, 31, 22, 33, 26);
        createBibleDto.setVerseList(verseList);

        BibleDto bible = bibleService.createBible(createBibleDto);
        System.out.println("bible = " + bible);
    }

//    @Test
//    public void getBibleTest(){
//        System.out.println(bibleService.getBibles());
//    }
}
