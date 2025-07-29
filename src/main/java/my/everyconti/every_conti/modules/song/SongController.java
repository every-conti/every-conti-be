package my.everyconti.every_conti.modules.song;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonPaginationDto;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.modules.song.dto.request.CreateSongDto;
import my.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import my.everyconti.every_conti.modules.song.dto.response.*;
import my.everyconti.every_conti.modules.song.dto.response.song.SongWithPraiseTeamDto;
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
    public ResponseEntity<CommonPaginationDto> searchSong(@Valid @ModelAttribute SearchSongDto searchSongDto) {
        return ResponseEntity.ok(songService.searchSong(searchSongDto));
    }

    // 찬양 삭제
    @DeleteMapping("/{innerSongId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<String>> createSong(@PathVariable Long innerSongId) {
        return ResponseEntity.ok(songService.deleteSong(innerSongId));
    }

    @GetMapping("/search/properties")
    public ResponseEntity<SearchPropertiesDto> getSearchProperties(){
        return ResponseEntity.ok(songService.getSearchProperties());
    }

    @GetMapping("/lasts")
    public ResponseEntity<List<SongWithPraiseTeamDto>> getLastFourSongs(){
        return ResponseEntity.ok(songService.getLastFourSongs());
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
