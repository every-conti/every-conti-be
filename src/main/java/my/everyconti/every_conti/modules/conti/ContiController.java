package my.everyconti.every_conti.modules.conti;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.conti.dto.request.CreateContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.UpdateContiOrderDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/conti")
@RequiredArgsConstructor
public class ContiController {

    private final ContiService contiService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiDto> createConti(@Valid @RequestBody CreateContiDto createContiDto){
         return ResponseEntity.ok(contiService.createConti(createContiDto));
    }

    @GetMapping("/{contiId}")
    public ResponseEntity<ContiDto> getContiDetail(@PathVariable String contiId){
        return ResponseEntity.ok(contiService.getContiDetail(contiId));
    }

    @PostMapping("/{contiId}/{songId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiDto> addSongToConti(@PathVariable String contiId, @PathVariable String songId) {
        return ResponseEntity.ok(contiService.addSongToConti(contiId, songId));
    }

    @PatchMapping("/{contiId}/songs/order")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ContiDto> updateContiOrder(@Valid @PathVariable String contiId, @RequestBody UpdateContiOrderDto updateContiOrderDto){
        return ResponseEntity.ok(contiService.updatecontiOrder(contiId, updateContiOrderDto));
    }
}
