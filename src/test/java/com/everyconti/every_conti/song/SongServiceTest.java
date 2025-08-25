package com.everyconti.every_conti.song;

import jakarta.transaction.Transactional;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class SongServiceTest {

    @Autowired
    SongService songService;
    @Autowired
    private HashIdUtil hashIdUtil;
//

//    @Test
//    @Commit
//    public void deleteSong(){
//        songService.deleteSong(31L);
//    }

//    @Test
//    @Commit
//    public void createSong(){
//        List<String> themeIds = new ArrayList<>();
//        themeIds.add(hashIdUtil.encode(5L));
//        CreateSongDto dto = CreateSongDto.builder()
//                .songName("태초에 하나님이")
//                .lyrics("태초에 하나님이 세상 모든 만물을\n" +
//                        "말씀으로 만드시고 그 만물 보고 기뻐하네\n" +
//                        "이 모든 만물은 하나님 위하여\n" +
//                        "찬양하게 만드셨네\n" +
//                        "목소리 높여서 하나님 찬양해\n" +
//                        "모든 만물이 주 찬양해")
//                .reference("https://www.youtube.com/watch?v=SB1QslVtedo&list=RDSB1QslVtedo&start_radio=1")
//                .thumbnail("https://img.youtube.com/vi/SB1QslVtedo/0.jpg")
//                .songType(SongType.CCM)
//                .tempo(SongTempo.FAST)
//                .themeIds(themeIds)
//                .creatorId(hashIdUtil.encode(4L))
//                .praiseTeamId(hashIdUtil.encode(1L))
//                .bibleId(hashIdUtil.encode(3L))
//                .bibleChapterId(hashIdUtil.encode(201L))
////                .bibleVerseId(hashIdUtil.encode(1L))
//                .build();
//
//        SongDto result = songService.createSong(dto);
//        System.out.println("result = " + result);
//    }

//    @Test
//    public void searchSong(){
//        List<Integer> themeIds = new ArrayList<>();
//        themeIds.add(1);
//        SearchSongDto dto = SearchSongDto.builder()
////                .text("song")
////                .songType(SongType.CCM)
////                .tempo(SongTempo.MEDIUM)
////                .themeIds(themeIds)
////                .praiseTeamId(1)
////                .seasonId(1)
////                .bibleBook("Psalms")
////                .bibleChapter(8)
////                .offset(0)
//                .build();
//
//        List<SongDto> result = songService.searchSong(dto);
//        System.out.println("result = " + result);
//    }
}
