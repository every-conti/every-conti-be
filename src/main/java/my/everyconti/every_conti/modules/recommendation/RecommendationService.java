package my.everyconti.every_conti.modules.recommendation;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.recommendation.domain.CoUsedSongStat;
import my.everyconti.every_conti.modules.recommendation.dto.CoUsedSongStatDto;
import my.everyconti.every_conti.modules.recommendation.repository.coUsedSongStat.CoUsedSongStatRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CoUsedSongStatRepository coUsedSongStatRepository;
    private final HashIdUtil hashIdUtil;

    public List<CoUsedSongStatDto> getCoUsedSongs(String songId){
        List<CoUsedSongStat> coUsedSongs = coUsedSongStatRepository.findCoUsedSongsBySongId(hashIdUtil.decode(songId));
        return coUsedSongs.stream().map(s -> new CoUsedSongStatDto(s, hashIdUtil)).toList();
    }


}
