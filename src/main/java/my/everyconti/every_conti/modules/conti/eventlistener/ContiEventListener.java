package my.everyconti.every_conti.modules.conti.eventlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.everyconti.every_conti.common.exception.types.NotFoundException;
import my.everyconti.every_conti.modules.conti.domain.Conti;
import my.everyconti.every_conti.modules.conti.domain.es.ContiDocument;
import my.everyconti.every_conti.modules.conti.repository.conti.ContiRepository;
import my.everyconti.every_conti.modules.conti.repository.es.ContiSearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContiEventListener {
    private final ContiSearchRepository contiSearchRepository;
    private final ContiRepository contiRepository;

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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleContiUpdated(ContiUpdatedEvent e){
        log.info("Handling ContiUpdatedEvent for contiId={}", e.getId());

        // 1. DB에서 최신 Conti 로드
        Conti conti = contiRepository.findById(e.getId())
                .orElseThrow(() -> new NotFoundException("콘티를 찾을 수 없습니다. id=" + e.getId()));

        // 2. Document 변환
        ContiDocument doc = new ContiDocument(conti.getId(), conti.getTitle());

        // 3. 검색 인덱스 업데이트
        contiSearchRepository.save(doc);

        log.info("ContiDocument updated in search index. contiId={}", e.getId());
    }


}
