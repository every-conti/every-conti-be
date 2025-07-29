package my.everyconti.every_conti.common.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonPaginationDto<T> {
    private List<T> items;
    private Long nextOffset;
}
