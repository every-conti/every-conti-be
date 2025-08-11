package my.everyconti.every_conti.modules.conti;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonPaginationDto;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.modules.conti.dto.request.CreateContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.SearchContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.UpdateContiOrderDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiSimpleDto;
import my.everyconti.every_conti.modules.conti.dto.response.PraiseTeamContiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/conti")
@RequiredArgsConstructor
public class ContiController {

    private final ContiService contiService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiSimpleDto> createConti(@Valid @RequestBody CreateContiDto createContiDto){
         return ResponseEntity.ok(contiService.createConti(createContiDto));
    }

    @GetMapping("/{contiId}")
    public ResponseEntity<ContiSimpleDto> getContiDetail(@PathVariable String contiId){
        return ResponseEntity.ok(contiService.getContiDetail(contiId));
    }

    @DeleteMapping("/{contiId}")
    public ResponseEntity<CommonResponseDto<String>> deleteConti(@PathVariable String contiId){
        return ResponseEntity.ok(contiService.deleteConti(contiId));
    }

    @PostMapping("/{contiId}/{songId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiSimpleDto> addSongToConti(@PathVariable String contiId, @PathVariable String songId) {
        return ResponseEntity.ok(contiService.addSongToConti(contiId, songId));
    }

    @PatchMapping("/{contiId}/songs/order")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiSimpleDto> updateContiOrder(@Valid @PathVariable String contiId, @RequestBody UpdateContiOrderDto updateContiOrderDto){
        return ResponseEntity.ok(contiService.updateContiOrder(contiId, updateContiOrderDto));
    }

    @GetMapping("/praise-teams/famous/last-conti")
    public ResponseEntity<List<PraiseTeamContiDto>> getFamousPraiseTeamsLastConti(){
        return ResponseEntity.ok(contiService.getFamousPraiseTeamsLastConti());
    }

    // 찬양 검색
    @GetMapping("/search")
    public ResponseEntity<CommonPaginationDto<PraiseTeamContiDto>> searchSong(@Valid @ModelAttribute SearchContiDto searchContiDto) {
        return ResponseEntity.ok(contiService.searchContis(searchContiDto));
    }

//    @GetMapping("/praise-teams/famous")
//    public ResponseEntity<List<PraiseTeamDto>> getFamousPraiseTeamLists(){
//        return ResponseEntity.ok(contiService.getFamousPraiseTeamLists());
//    }
//
//    @GetMapping("/praise-teams/{praiseTeamId}/contis")
//    public ResponseEntity<List<ContiSimpleDto>> getContiListsByPraiseTeam(@PathVariable String praiseTeamId){
//        return ResponseEntity.ok(contiService.getContiListsByPraiseTeam(praiseTeamId));
//    }
}
