package my.everyconti.every_conti.modules.conti;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.common.exception.types.AlreadyExistElementException;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.common.utils.SecurityUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.conti.domain.*;
import my.everyconti.every_conti.modules.conti.dto.request.CreateContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.UpdateContiOrderDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiDto;
import my.everyconti.every_conti.modules.conti.repository.conti.ContiRepository;
import my.everyconti.every_conti.modules.conti.repository.ContiSongRepository;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import my.everyconti.every_conti.modules.song.domain.QSong;
import my.everyconti.every_conti.modules.song.domain.QSongSongTheme;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.repository.song.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ContiService {
    private final ContiRepository contiRepository;
    private final MemberRepository memberRepository;
    private final HashIdUtil hashIdUtil;
    private final JPAQueryFactory queryFactory;
    private final SongRepository songRepository;
    private final ContiSongRepository contiSongRepository;

    @Transactional
    public ContiDto createConti(CreateContiDto createContiDto){
        Member member = memberRepository.findById(hashIdUtil.decode(createContiDto.getMemberId())).orElseThrow();

        Conti conti = Conti.builder()
                .title(createContiDto.getTitle())
                .date(createContiDto.getDate())
                .creator(member)
                .build();

        Conti createdConti = contiRepository.save(conti);
        return new ContiDto(createdConti, hashIdUtil);
    }

    public ContiDto getContiDetail(String contiId){
        QConti conti = QConti.conti;
        QContiSong  contiSong = QContiSong.contiSong;
        QSong song = QSong.song;
        QSongSongTheme songSongTheme = QSongSongTheme.songSongTheme;

        Conti searchedConti = queryFactory
                .selectFrom(conti)
                .leftJoin(conti.contiSongs, contiSong).fetchJoin()
                .leftJoin(contiSong.song, song).fetchJoin()
                .leftJoin(song.praiseTeam).fetchJoin()
                .leftJoin(song.songThemes, songSongTheme).fetchJoin()
                .leftJoin(songSongTheme.songTheme).fetchJoin()
                .leftJoin(conti.creator).fetchJoin()
                .where(conti.id.eq(hashIdUtil.decode(contiId)))
                .distinct()
                .fetch()
                .getFirst();

        return new ContiDto(searchedConti, hashIdUtil);
    }

    @Transactional
    public ContiDto addSongToConti(String contiId, String songId){
        Long innerContiId = hashIdUtil.decode(contiId);
        Long innerSongId = hashIdUtil.decode(songId);

        Conti conti = contiRepository.getContiByIdWithJoin(innerContiId);
        // creator인지 확인
        SecurityUtil.checkCreatorOrAdmin();

        boolean alreadyExists = conti.getContiSongs().stream()
                .anyMatch(cs -> cs.getSong().getId().equals(innerSongId));
        if (alreadyExists) {
            throw new AlreadyExistElementException(ResponseMessage.CONFLICT);
        }
        Song song = songRepository.getSongByIdWithJoin(innerSongId);

        Integer nextOrder = conti.getContiSongs().stream()
                .mapToInt(ContiSong::getOrderIndex)
                .max()
                .orElse(0) + 1;

        ContiSong contiSong = ContiSong.builder()
                .conti(conti)
                .song(song)
                .orderIndex(nextOrder)
                .build();

        conti.getContiSongs().add(contiSong);
        contiSongRepository.save(contiSong);
        contiRepository.save(conti);

        return new ContiDto(conti, hashIdUtil);
    }

    @Transactional
    public ContiDto updatecontiOrder(String contiId, UpdateContiOrderDto updateContiOrderDto){
        Conti conti = contiRepository.getContiAndContiSongByContiId(hashIdUtil.decode(contiId));

        // creator인지 확인
        SecurityUtil.checkCreatorOrAdmin();

        List<String> contiSongIds = updateContiOrderDto.getContiSongIds();
        List<Long> contiSongIdsLong =  contiSongIds.stream().map(id -> hashIdUtil.decode(id)).toList();

        if (contiSongIds.size() != conti.getContiSongs().size()) {
            throw new IllegalArgumentException("전달된 곡 ID 목록이 기존과 일치하지 않습니다.");
        }

        // 순서 재설정
        Map<Long, ContiSong> songMap = conti.getContiSongs().stream()
                .collect(Collectors.toMap(ContiSong::getId, cs -> cs));

        for (int i = 0; i < contiSongIdsLong.size(); i++) {
            Long id = contiSongIdsLong.get(i);
            ContiSong cs = songMap.get(id);
            cs.setOrderIndex(i+1);
        }

        List<ContiSong> updatedContiSongs = contiSongRepository.saveAll(songMap.values());

        return new ContiDto(conti, hashIdUtil);
    }
}
