package my.everyconti.every_conti.modules.conti;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import my.everyconti.every_conti.modules.conti.dto.request.*;
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
    @PersistenceContext
    private EntityManager em;


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
    public ContiWithSongDto updateConti(String contiIdHashed, UpdateContiDto dto) {
        Long contiId = hashIdUtil.decode(contiIdHashed);
        Conti conti = contiRepository.getContiAndContiSongByContiId(contiId);
        if (conti == null) throw new NotFoundException("콘티를 찾을 수 없습니다.");

        SecurityUtil.userMatchOrAdmin(conti.getCreator().getEmail());

        if (dto.getTitle() != null) conti.setTitle(dto.getTitle());
        if (dto.getDescription() != null) conti.setDescription(dto.getDescription());
        if (dto.getDate() != null) conti.setDate(dto.getDate());

        if (dto.getSongIds() != null) {
            // 1) 기존 곡 모두 삭제 (bulk) + 즉시 flush
            contiSongRepository.deleteByContiId(contiId);
            conti.getContiSongs().clear();
            em.flush();
            em.clear();

            // 2) 요청된 곡들 준비 (중복 제거 + 순서 유지)
            List<String> dedupHashed = new ArrayList<>(new LinkedHashSet<>(dto.getSongIds()));
            List<Long> songIds = dedupHashed.stream().map(hashIdUtil::decode).toList();

            // 3) 존재 검증
            List<Song> songs = songRepository.findAllById(songIds);
            if (songs.size() != songIds.size()) {
                Set<Long> found = songs.stream().map(Song::getId).collect(Collectors.toSet());
                List<String> missing = new ArrayList<>();
                for (String h : dedupHashed) {
                    Long id = hashIdUtil.decode(h);
                    if (!found.contains(id)) missing.add(h);
                }
                throw new IllegalArgumentException("존재하지 않는 곡 ID 포함: " + missing);
            }
            Map<Long, Song> songMap = songs.stream()
                    .collect(Collectors.toMap(Song::getId, Function.identity()));

            // 4) 새 insert (orderIndex 0..N-1 또는 1..N으로 "통일")
            List<ContiSong> toInsert = new ArrayList<>(songIds.size());
            for (int i = 0; i < songIds.size(); i++) {
                Long sid = songIds.get(i);
                toInsert.add(
                        ContiSong.builder()
                                .conti(conti)
                                .song(songMap.get(sid))
                                .orderIndex(i)
                                .build()
                );
            }
            contiSongRepository.saveAll(toInsert);
            conti.getContiSongs().addAll(toInsert);
        }

        contiRepository.save(conti);
        return new ContiWithSongDto(conti, hashIdUtil);
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
