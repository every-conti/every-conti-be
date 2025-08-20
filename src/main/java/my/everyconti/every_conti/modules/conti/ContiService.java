package my.everyconti.every_conti.modules.conti;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonPaginationDto;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.common.exception.types.AlreadyExistElementException;
import my.everyconti.every_conti.common.exception.types.NotFoundException;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.common.utils.SecurityUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.conti.domain.*;
import my.everyconti.every_conti.modules.conti.dto.request.CopyContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.CreateContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.SearchContiDto;
import my.everyconti.every_conti.modules.conti.dto.request.UpdateContiOrderDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiPropertiesDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiSimpleDto;
import my.everyconti.every_conti.modules.conti.dto.response.ContiWithSongDto;
import my.everyconti.every_conti.modules.conti.eventlistener.ContiCreatedEvent;
import my.everyconti.every_conti.modules.conti.repository.conti.ContiRepository;
import my.everyconti.every_conti.modules.conti.repository.contiSong.ContiSongRepository;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.member.MemberRepository;
import my.everyconti.every_conti.modules.song.domain.Song;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;
import my.everyconti.every_conti.modules.song.repository.PraiseTeamRepository;
import my.everyconti.every_conti.modules.song.repository.song.SongRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static my.everyconti.every_conti.constant.ResponseMessage.notFoundMessage;


@Service
@AllArgsConstructor
public class ContiService {
    private final ContiRepository contiRepository;
    private final MemberRepository memberRepository;
    private final HashIdUtil hashIdUtil;
    private final SongRepository songRepository;
    private final ContiSongRepository contiSongRepository;
    private final PraiseTeamRepository praiseTeamRepository;
    private final ApplicationEventPublisher publisher;


    @Transactional
    public ContiSimpleDto createConti(CreateContiDto createContiDto){
        Member member = memberRepository
                .findById(hashIdUtil.decode(createContiDto.getMemberId()))
                .orElseThrow();

        // 자신의 이메일인지 확인
        SecurityUtil.userMatchOrAdmin(member.getEmail());

        Conti conti = Conti.builder()
                .title(createContiDto.getTitle())
                .description(createContiDto.getDescription())
                .date(createContiDto.getDate())
                .creator(member)
                .build();

        Conti createdConti = contiRepository.save(conti);

        // === 곡 추가 시작 ===
        List<String> reqSongIds = createContiDto.getSongIds();
        if (reqSongIds != null && !reqSongIds.isEmpty()) {

            // 1) 중복 제거(입력 순서 유지)
            List<String> dedupHashedIds = new ArrayList<>(new LinkedHashSet<>(reqSongIds));

            // 2) 해시 → Long
            List<Long> songIdLongs = dedupHashedIds.stream()
                    .map(hashIdUtil::decode)
                    .toList();

            // 3) 존재하는 곡 일괄 조회
            List<Song> songs = songRepository.findAllById(songIdLongs);
            Map<Long, Song> songMap = songs.stream()
                    .collect(Collectors.toMap(Song::getId, Function.identity()));

            // 4) 검증 (요청 중 존재하지 않는 곡이 있는지)
            List<String> notFound = new ArrayList<>();
            for (String hashed : dedupHashedIds) {
                Long id = hashIdUtil.decode(hashed);
                if (!songMap.containsKey(id)) {
                    notFound.add(hashed);
                }
            }
            if (!notFound.isEmpty()) {
                throw new IllegalArgumentException("존재하지 않는 곡 ID가 포함되어 있습니다: " + notFound);
            }

            // 5) ContiSong 생성(입력 순서대로 orderIndex 부여)
            List<ContiSong> contiSongs = new ArrayList<>(dedupHashedIds.size());
            int order = 0;
            for (String hashed : dedupHashedIds) {
                Long id = hashIdUtil.decode(hashed);
                Song song = songMap.get(id);

                ContiSong cs = ContiSong.builder()
                        .conti(createdConti)
                        .song(song)
                        .orderIndex(order++) // 0부터 시작, 필요시 1부터로 변경
                        .build();

                contiSongs.add(cs);
            }

            contiSongRepository.saveAll(contiSongs);
        }

        publisher.publishEvent(new ContiCreatedEvent(createdConti.getId(), createdConti.getTitle()));

        return new ContiSimpleDto(createdConti, hashIdUtil);
    }

    @Transactional
    public ContiSimpleDto copyConti(CopyContiDto dto) {
        // 0) 호출자 검증
        Member member = memberRepository
                .findById(hashIdUtil.decode(dto.getMemberId()))
                .orElseThrow(() -> new NotFoundException(notFoundMessage("유저")));
        SecurityUtil.userMatchOrAdmin(member.getEmail());

        // 1) IDs 디코드
        Long srcContiId = hashIdUtil.decode(dto.getCopiedContiId());  // 곡을 가져올 콘티
        Long dstContiId = hashIdUtil.decode(dto.getTargetContiId());  // 곡을 붙일 콘티

        if (srcContiId.equals(dstContiId)) {
            // 자기 자신에게 복사면 할 게 없음
            Conti same = contiRepository.findById(dstContiId)
                    .orElseThrow(() -> new NotFoundException(notFoundMessage("콘티")));
            return new ContiSimpleDto(same, hashIdUtil);
        }

        // 2) 원본/대상 콘티 로드
        Conti src = contiRepository.findById(srcContiId)
                .orElseThrow(() -> new NotFoundException(notFoundMessage("복사할 콘티")));
        Conti dst = contiRepository.findById(dstContiId)
                .orElseThrow(() -> new NotFoundException(notFoundMessage("대상 콘티")));

        // 3) 원본 곡 목록(순서대로) 조회
        List<ContiSong> srcSongs = contiSongRepository
                .findAllByContiIdOrderByOrderIndexAsc(src.getId());

        if (srcSongs.isEmpty()) {
            // 원본에 곡이 없으면 그대로 반환
            return new ContiSimpleDto(dst, hashIdUtil);
        }

        // 4) 대상 콘티의 마지막 orderIndex 및 기존 포함 곡 수집
        int maxOrder = Optional.ofNullable(
                contiSongRepository.findMaxOrderIndexByContiId(dst.getId())
        ).orElse(-1);
        Set<Long> existingSongIds = contiSongRepository.findSongIdsByContiId(dst.getId());

        // 5) Append 목록 생성 (중복 곡은 스킵)
        List<ContiSong> toAppend = new ArrayList<>(srcSongs.size());
        for (ContiSong cs : srcSongs) {
            Long songId = cs.getSong().getId();

            // 중복 허용하려면 이 조건을 제거
            if (existingSongIds.contains(songId)) continue;

            ContiSong clone = ContiSong.builder()
                    .conti(dst)
                    .song(cs.getSong())
                    .orderIndex(++maxOrder)
                    .build();
            toAppend.add(clone);
        }

        if (!toAppend.isEmpty()) {
            contiSongRepository.saveAll(toAppend);
            // 필요 시 이벤트
            // publisher.publishEvent(new ContiUpdatedEvent(dst.getId(), "songs_appended_from_conti_copy"));
        }

        return new ContiSimpleDto(dst, hashIdUtil);
    }



    public CommonPaginationDto<ContiWithSongDto> getMyContiList(String memberId, Long offset){
        Member member = memberRepository.findById(hashIdUtil.decode(memberId)).orElseThrow();

        SecurityUtil.userMatchOrAdmin(member.getEmail());

        List<Conti> resultList = contiRepository.findContisWithMemberId(member.getId(), offset);
        List<ContiWithSongDto> data = resultList.stream().map(c -> new ContiWithSongDto(c, hashIdUtil)).toList();

        Long nextOffset = offset;
        if (nextOffset != null && resultList.size() == 11) nextOffset += 11;
        else nextOffset = null;

        return new CommonPaginationDto<>(data, nextOffset);
    }

    public ContiWithSongDto getContiDetail(String contiId){
        Conti conti = contiRepository.getContiDetail(hashIdUtil.decode(contiId));
        return new ContiWithSongDto(conti, hashIdUtil);
    }

    @Transactional
    public ContiSimpleDto addSongToConti(String contiId, String songId){
        Long innerContiId = hashIdUtil.decode(contiId);
        Long innerSongId = hashIdUtil.decode(songId);

        Conti conti = contiRepository.getContiByIdWithJoin(innerContiId);
        // creator인지 확인
        SecurityUtil.userMatchOrAdmin(conti.getCreator().getEmail());

        boolean alreadyExists = conti.getContiSongs().stream()
                .anyMatch(cs -> cs.getSong().getId().equals(innerSongId));
        if (alreadyExists) {
            throw new AlreadyExistElementException(ResponseMessage.CONFLICT);
        }
        Song song = songRepository.findSongByIdWithJoin(innerSongId);

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

        return new ContiSimpleDto(conti, hashIdUtil);
    }

    @Transactional
    public ContiSimpleDto updateContiOrder(String contiId, UpdateContiOrderDto updateContiOrderDto){
        Conti conti = contiRepository.getContiAndContiSongByContiId(hashIdUtil.decode(contiId));

        // creator인지 확인
        SecurityUtil.userMatchOrAdmin(conti.getCreator().getEmail());

        List<String> contiSongIds = updateContiOrderDto.getContiSongIds();
        List<Long> contiSongIdsLong = contiSongIds.stream().map(id -> hashIdUtil.decode(id)).toList();

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

        contiSongRepository.saveAll(songMap.values());

        return new ContiSimpleDto(conti, hashIdUtil);
    }

    public CommonResponseDto<String> deleteConti(String contiId){
        Conti conti = contiRepository.findContiById(hashIdUtil.decode(contiId));

        // 소유자 or admin확인
        SecurityUtil.userMatchOrAdmin(conti.getCreator().getEmail());

        contiRepository.delete(conti);
        return new CommonResponseDto<>(true, ResponseMessage.DELETED);
    }

    public List<ContiWithSongDto> getFamousPraiseTeamsLastConti(){
        List<Conti> lastContiOfFamousPraiseTeams = contiRepository.findLastContiOfFamousPraiseTeams();
        List<ContiWithSongDto> contis =  lastContiOfFamousPraiseTeams.stream().map(c -> new ContiWithSongDto(c, hashIdUtil)).toList();
        return contis;
    }

    public CommonPaginationDto<ContiWithSongDto> searchContis(SearchContiDto searchContiDto){
        List<Conti> resultList = contiRepository.findContisWithSearchParams(searchContiDto);
        List<ContiWithSongDto> data = resultList.stream().map(c -> new ContiWithSongDto(c, hashIdUtil)).toList();

        Long nextOffset = searchContiDto.getOffset();
        if (nextOffset != null && resultList.size() == 21) nextOffset += 21;
        else nextOffset = null;

        return new CommonPaginationDto<>(data, nextOffset);
    }

    public ContiPropertiesDto getContiProperties(){
        List<PraiseTeamDto> praiseTeams = praiseTeamRepository.findAll().stream().map(t -> new PraiseTeamDto(t, hashIdUtil)).toList();
        return ContiPropertiesDto
                .builder()
                .praiseTeams(praiseTeams)
    //                    .seasons(getSeasonLists())
                .build();
    }
//    public List<PraiseTeamDto> getFamousPraiseTeamLists(){
//        List<PraiseTeam> famousPraiseTeams = praiseTeamRepository.findPraiseTeamsByIsFamousTrue();
//
//        return famousPraiseTeams.stream().map(t -> new PraiseTeamDto(t, hashIdUtil)).collect(Collectors.toList());
//    }
//
//    public List<ContiSimpleDto> getContiListsByPraiseTeam(String praiseTeamId){
//        List<Conti> praiseTeamContis = contiRepository.findContisByPraiseTeam_Id(hashIdUtil.decode(praiseTeamId));
//
//        return praiseTeamContis.stream().map(c -> new ContiSimpleDto(c, hashIdUtil)).collect(Collectors.toList());
//    }
}
