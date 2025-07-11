package my.everyconti.every_conti.song;

import jakarta.transaction.Transactional;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;
import my.everyconti.every_conti.modules.song.SongService;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.request.CreateSongDto;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import my.everyconti.every_conti.modules.song.dto.response.SongDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class SongServiceTest {

    @Autowired
    SongService songService;

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

    @Test
    public void searchSong(){
        List<Integer> themeIds = new ArrayList<>();
        themeIds.add(1);
        SearchSongDto dto = SearchSongDto.builder()
//                .text("song")
//                .songType(SongType.CCM)
//                .tempo(SongTempo.MEDIUM)
//                .themeIds(themeIds)
//                .praiseTeamId(1)
//                .seasonId(1)
                .bibleBook("Psalms")
//                .bibleChapter(8)
//                .offset(0)
                .build();

        List<SongDto> result = songService.searchSong(dto);
        System.out.println("result = " + result);
    }
}
