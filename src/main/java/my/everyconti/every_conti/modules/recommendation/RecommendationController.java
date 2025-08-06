package my.everyconti.every_conti.modules.recommendation;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.recommendation.dto.CoUsedSongStatWithSongInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/co-used-songs/{songId}")
    public ResponseEntity<List<CoUsedSongStatWithSongInfoDto>> getCoUsedSongs(@PathVariable String songId) {
        return ResponseEntity.ok(recommendationService.getCoUsedSongs(songId));
    }
}
