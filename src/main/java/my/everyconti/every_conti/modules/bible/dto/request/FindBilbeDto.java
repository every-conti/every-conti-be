package my.everyconti.every_conti.modules.bible.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindBilbeDto {
    @NotBlank
    String bibleId;
}
