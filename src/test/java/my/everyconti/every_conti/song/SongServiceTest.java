package my.everyconti.every_conti.song;

import jakarta.transaction.Transactional;
//import my.everyconti.every_conti.modules.song.SongService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class SongServiceTest {

//    @Autowired
//    SongService songService;
//
//    @Test
//    void getBibleListTest() throws JSONException {
////        System.out.println("테스트 실행");
//        long start = System.currentTimeMillis();
//        List<BibleDto> result = songService.getBibleList();
//        long end = System.currentTimeMillis();
//        System.out.println("time: " + (end - start) + "ms");
//        System.out.println("result = " + result);
//    }

//    @Test
//    public void createSong(){
//        List<Integer> themeIds = new ArrayList<>();
//        themeIds.add(1);
//        CreateSongDto dto = CreateSongDto.builder()
//                .songName("test song")
//                .lyrics("lalalalala lololololo lalalalalalal")
//                .reference("https://www.youtube.com/watch?v=4buUHFAzH_Y")
//                .songType(SongType.CCM)
//                .tempo(SongTempo.MEDIUM)
//                .themeIds(themeIds)
//                .creatorId(3)
//                .praiseTeamId(1)
//                .seasonId(1)
//                .bibleBook("Psalms")
//                .bibleChapter(8)
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
