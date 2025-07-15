package my.everyconti.every_conti.modules.bible;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.bible.dto.request.CreateBibleDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleChapterDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleDto;
import my.everyconti.every_conti.modules.bible.dto.response.BibleVerseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bible")
@RequiredArgsConstructor
public class BibleController {


    private final BibleService bibleService;

    // 성경 생성
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<BibleDto> createBible(@Valid @RequestBody CreateBibleDto createBibleDto) {
        return ResponseEntity.ok(bibleService.createBible(createBibleDto));
    }

    @GetMapping("/{bibleId}/chapters")
    public ResponseEntity<List<BibleChapterDto>> getChapterListsByBibleId(@PathVariable String bibleId){
        return ResponseEntity.ok(bibleService.getChapterListsByBibleId(bibleId));
    }

    @GetMapping("/{chapterId}/verses")
    public ResponseEntity<List<BibleVerseDto>> getVerseListsByChapterId(@PathVariable String chapterId){
        return ResponseEntity.ok(bibleService.getVerseListsByChapterId(chapterId));
    }
}
