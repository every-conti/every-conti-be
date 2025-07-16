package my.everyconti.every_conti.modules.conti.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateContiOrderDto {
    private List<String> contiSongIds;
}
