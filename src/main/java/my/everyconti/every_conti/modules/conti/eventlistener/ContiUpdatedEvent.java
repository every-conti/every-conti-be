package my.everyconti.every_conti.modules.conti.eventlistener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContiUpdatedEvent {
    private Long id;
    private String title;

}
