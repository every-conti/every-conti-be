package my.everyconti.every_conti.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {

    private boolean success;
    private T data;

    @Override
    public String toString() {
        return "CommonResponseDto{" +
                "success='" + success + '\'' +
                ", data='" + data.toString() + '\'' +
                '}';
    }
}