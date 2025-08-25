package com.everyconti.every_conti.modules.conti.repository.contiSong;

import com.everyconti.every_conti.modules.conti.domain.ContiSong;

import java.util.List;
import java.util.Set;

public interface ContiSongRepositoryCustom {
    List<ContiSong> findAllByContiIdOrderByOrderIndexAsc(Long contiId);
    Integer findMaxOrderIndexByContiId(Long contiId);
    Set<Long> findSongIdsByContiId(Long contiId);
}
