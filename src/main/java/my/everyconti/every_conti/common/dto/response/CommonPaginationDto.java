package my.everyconti.every_conti.common.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CommonPaginationDto<T> {
    private List<T> items;
    private Long nextOffset;

    public CommonPaginationDto(List<T> items, Long nextOffset) {
        this.items = items;
        this.nextOffset = nextOffset;
    }
}
