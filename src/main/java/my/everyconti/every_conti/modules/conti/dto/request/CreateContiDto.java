package my.everyconti.every_conti.modules.conti.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CreateContiDto {
    @NotBlank
    @Size(max = 100)
    private String title;

    private LocalDate date;

    private String memberId;
}
