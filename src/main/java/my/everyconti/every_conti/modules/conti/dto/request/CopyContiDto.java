package my.everyconti.every_conti.modules.conti.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CopyContiDto {
    private String memberId;
    private String copiedContiId;
    private String targetContiId;
    private List<String> songIds;
}
