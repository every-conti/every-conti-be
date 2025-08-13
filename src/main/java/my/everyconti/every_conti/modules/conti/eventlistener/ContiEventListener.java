package my.everyconti.every_conti.modules.conti.eventlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.everyconti.every_conti.modules.conti.domain.es.ContiDocument;
import my.everyconti.every_conti.modules.conti.repository.es.ContiSearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContiEventListener {
    private final ContiSearchRepository contiSearchRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleContiCreated(ContiCreatedEvent e){
        log.info("Conti Created Event");
        contiSearchRepository.save(new ContiDocument(e.getId(), e.getTitle()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleContiDeleted(ContiDeletedEvent e){
        log.info("Conti Created Event");
        contiSearchRepository.save(new ContiDocument(e.getId(), e.getTitle()));
    }


}
