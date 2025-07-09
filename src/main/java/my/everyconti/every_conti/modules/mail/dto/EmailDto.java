package my.everyconti.every_conti.modules.mail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String email;
}