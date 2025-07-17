package my.everyconti.every_conti.modules.recommendation.domain;

import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.common.utils.HashIdUtil;

@Entity
@Table(name = "co_used_song_stat",
        indexes = {@Index(name = "index_song_id_a", columnList = "song_id_a", unique = true), @Index(name = "index_song_id_b", columnList = "song_id_b", unique = true)},
        uniqueConstraints = @UniqueConstraint(columnNames = {"song_id_a", "song_id_b"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
public class CoUsedSongStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "song_id_a")
    private Long songIdA;  // 항상 정렬된 쪽 먼저

    @Column(name = "song_id_b")
    private Long songIdB;

    @Column(name = "usage_count")
    private Long usageCount = 0L;

    public CoUsedSongStat(Long id, Long songId1, Long songId2, Long usageCount) {
        this.id = id;
        this.songIdA = songId1;
        this.songIdB = songId2;
        this.usageCount = usageCount == null ? 1L : usageCount;
    }

    public static CoUsedSongStat of(Long id, Long songId1, Long songId2, Long usageCount) {
        Long id1 = songId1;
        Long id2 = songId2;
        // 정렬하여 항상 같은 순서로 저장
        return new CoUsedSongStat(id, Math.min(id1, id2), Math.max(id1, id2), usageCount);
    }
}