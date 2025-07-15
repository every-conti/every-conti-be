package my.everyconti.every_conti.modules.bible.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateBibleDto {
    @NotBlank
    private String bibleEnName;
    @NotBlank
    private String bibleKoName;
    @NotNull
    private Integer chaptersCount;
    @NotNull
    private List<Integer> verseList;
}
