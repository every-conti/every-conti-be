package com.everyconti.every_conti.modules.song;

import com.everyconti.every_conti.modules.song.dto.response.SongDto;
import com.everyconti.every_conti.modules.song.dto.response.SongPropertiesDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.everyconti.every_conti.common.dto.response.CommonPaginationDto;
import com.everyconti.every_conti.common.dto.response.CommonResponseDto;
import com.everyconti.every_conti.modules.song.dto.request.CreateSongDto;
import com.everyconti.every_conti.modules.song.dto.request.SearchSongDto;
import com.everyconti.every_conti.modules.song.dto.response.song.MinimumSongToPlayDto;
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

    @GetMapping("/lasts")
    public ResponseEntity<List<MinimumSongToPlayDto>> getLastFourSongs(){
        return ResponseEntity.ok(songService.getLastFourSongs());
    }

    // 찬양 생성／검색을 위한 조건
    @GetMapping("/properties")
    public ResponseEntity<SongPropertiesDto> getSongProperties(){
        return ResponseEntity.ok(songService.getSongProperties());
    }

    @GetMapping("/detail/{songId}")
    public ResponseEntity<SongDto> getSongDetailInfo(@PathVariable("songId") String songId){
        return ResponseEntity.ok(songService.getSongDetailInfo(songId));
    }

    @GetMapping("/youtube-v-id/check/{youtubeVId}")
    public ResponseEntity<CommonResponseDto<Boolean>> checkYoutubeVId(@PathVariable String youtubeVId){
        return ResponseEntity.ok(songService.checkYoutubeVId(youtubeVId));
    }

    // 찬양 검색
    @GetMapping("/search")
    public ResponseEntity<CommonPaginationDto<SongDto>> searchSong(@Valid @ModelAttribute SearchSongDto searchSongDto) {
        return ResponseEntity.ok(songService.searchSong(searchSongDto));
    }

    // 찬양 삭제
    @DeleteMapping("/{innerSongId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<String>> deleteSong(@PathVariable Long innerSongId) {
        return ResponseEntity.ok(songService.deleteSong(innerSongId));
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
