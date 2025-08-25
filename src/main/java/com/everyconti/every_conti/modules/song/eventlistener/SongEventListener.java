package com.everyconti.every_conti.modules.song.eventlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.everyconti.every_conti.modules.song.domain.es.SongDocument;
import com.everyconti.every_conti.modules.song.repository.es.SongSearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SongEventListener {
    private final SongSearchRepository songSearchRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSongCreated(SongCreatedEvent e){
        log.info("Song Created Event");

        songSearchRepository.save(SongDocument.builder()
                .id(e.getId())
                .songName(e.getSongName())
                .lyrics(e.getLyrics())
                .build());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSongDeleted(SongDeletedEvent e){
        log.info("Song Created Event");
        songSearchRepository.deleteById(e.getId());
    }
}
