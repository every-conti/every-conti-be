package my.everyconti.every_conti.modules.conti.eventlistener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContiCreatedEvent {
    private Long id;
    private String title;
}
