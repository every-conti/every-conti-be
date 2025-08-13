package my.everyconti.every_conti.modules.song.eventlistener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SongCreatedEvent {
    private Long id;
    private String songName;
    private String lyrics;
}
