package my.everyconti.every_conti.modules.conti;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonPaginationDto;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.modules.conti.dto.request.CopyContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.CreateContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.SearchContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.UpdateContiOrderDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiPropertiesDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiSimpleDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiWithSongDto;
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
    public ResponseEntity<ContiSimpleDto> createConti(@Valid @RequestBody CreateContiDto createContiDto) {
         return ResponseEntity.ok(contiService.createConti(createContiDto));
    }

    @PostMapping("/copy")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiSimpleDto> copyConti(@Valid @RequestBody CopyContiDto copyContiDto){
        return ResponseEntity.ok(contiService.copyConti(copyContiDto));
    }

    @GetMapping("/{memberId}/mine")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonPaginationDto<ContiWithSongDto>> getMyContiList(@PathVariable String memberId, @RequestParam Long offset){
        return ResponseEntity.ok(contiService.getMyContiList(memberId, offset));
    }

    @GetMapping("/{contiId}")
    public ResponseEntity<ContiWithSongDto> getContiDetail(@PathVariable String contiId){
        return ResponseEntity.ok(contiService.getContiDetail(contiId));
    }

    @DeleteMapping("/{contiId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    public ResponseEntity<List<ContiWithSongDto>> getFamousPraiseTeamsLastConti(){
        return ResponseEntity.ok(contiService.getFamousPraiseTeamsLastConti());
    }

    @GetMapping("/search")
    public ResponseEntity<CommonPaginationDto<ContiWithSongDto>> searchSong(@Valid @ModelAttribute SearchContiDto searchContiDto) {
        return ResponseEntity.ok(contiService.searchContis(searchContiDto));
    }

    // 콘티 검색을 위한 조건
    @GetMapping("/properties")
    public ResponseEntity<ContiPropertiesDto> getContiProperties(){
        return ResponseEntity.ok(contiService.getContiProperties());
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
