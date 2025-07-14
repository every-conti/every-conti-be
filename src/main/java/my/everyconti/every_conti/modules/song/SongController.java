package my.everyconti.every_conti.modules.song;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.modules.song.dto.request.CreateSongDto;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import my.everyconti.every_conti.modules.song.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    // 찬양 생성
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SongDto> createSong(@Valid @RequestBody CreateSongDto createSongDto) {
        return ResponseEntity.ok(songService.createSong(createSongDto));
    }

    // 찬양 검색
    @GetMapping("/search")
    public ResponseEntity<List<SongDto>> searchSong(@Valid @ModelAttribute SearchSongDto searchSongDto) {
        System.out.println("컨트롤러 도달");
        return ResponseEntity.ok(songService.searchSong(searchSongDto));
    }

    // 찬양 삭제
    @DeleteMapping("/{innerSongId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<String>> createSong(@PathVariable Long innerSongId) {
        return ResponseEntity.ok(songService.deleteSong(innerSongId));
    }

    @GetMapping("/searchProperties")
    public ResponseEntity<SearchPropertiesDto> getSearchProperties(){
        return ResponseEntity.ok(songService.getSearchProperties());
    }
//    @GetMapping("/praiseTeams")
//    public ResponseEntity<List<PraiseTeamDto>> getPraiseTeamLists(){
//        return ResponseEntity.ok(songService.getPraiseTeamLists());
//    }
//    @GetMapping("/seasons")
//    public ResponseEntity<List<SeasonDto>> getSeasonLists(){
//        return ResponseEntity.ok(songService.getSeasonLists());
//    }
//    @GetMapping("/themes")
//    public ResponseEntity<List<SongThemeDto>> getSongThemeLists(){
//        return ResponseEntity.ok(songService.getSongThemeLists());
//    }

    // 찬양 신고
//    @PostMapping("/report/{songId}")
//    @PreAuthorize("hasAnyRole('USER')")
//    public ResponseEntity<CommonResponseDto<String>> reportSong(@PathVariable ReportSongDto reportSongDto){
//        return songService.reportSong(reportSongDto);
//    }
}
