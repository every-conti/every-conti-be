package com.everyconti.every_conti.modules.recommendation;

import lombok.RequiredArgsConstructor;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import com.everyconti.every_conti.modules.recommendation.dto.CoUsedSongStatWithSongInfoDto;
import com.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat.CoUsedSongStatRepository;
import com.everyconti.every_conti.modules.song.domain.Song;
import com.everyconti.every_conti.modules.song.repository.song.SongRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CoUsedSongStatRepository coUsedSongStatRepository;
    private final SongRepository songRepository;
    private final HashIdUtil hashIdUtil;

    public List<CoUsedSongStatWithSongInfoDto> getCoUsedSongs(String encodedSongId) {
        Long songId = hashIdUtil.decode(encodedSongId);
        List<CoUsedSongStat> coUsedStats = coUsedSongStatRepository.findCoUsedSongsBySongId(songId);

        Set<Long> songIdsToFetch = coUsedStats.stream()
                .map(stat -> !Objects.equals(stat.getSongIdA(), songId) ? stat.getSongIdA() : stat.getSongIdB())
                .collect(Collectors.toSet());

        Map<Long, Song> songMap = songRepository.findByIdIn(songIdsToFetch).stream()
                .collect(Collectors.toMap(Song::getId, s -> s));

        return coUsedStats.stream()
                .map(stat -> {
                    Long targetSongId = !Objects.equals(stat.getSongIdA(), songId) ? stat.getSongIdA() : stat.getSongIdB();
                    Song targetSong = songMap.get(targetSongId);

                    // song이 삭제되어 null인 경우는 제외
                    if (targetSong == null) return null;

                    return new CoUsedSongStatWithSongInfoDto(stat, targetSong, hashIdUtil);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
