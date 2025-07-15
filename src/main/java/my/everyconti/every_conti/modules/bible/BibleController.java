package my.everyconti.every_conti.modules.bible;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.bible.dto.request.CreateBibleDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bible")
@RequiredArgsConstructor
public class BibleController {


    private final BibleService bibleService;

    // 성경 생성
    @PostMapping("")
    public ResponseEntity<BibleDto> createBible(@Valid @RequestBody CreateBibleDto createBibleDto) {
        return ResponseEntity.ok(bibleService.createBible(createBibleDto));
    }
}
