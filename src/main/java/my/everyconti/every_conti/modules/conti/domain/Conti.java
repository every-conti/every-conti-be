package my.everyconti.every_conti.modules.conti.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.song.domain.PraiseTeam;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conti extends NowTimeForJpa {
    @JsonIgnore
    @Id
    @Column(name = "conti_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "conti", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContiSong> contiSongs;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "praise_team")
    private PraiseTeam praiseTeam;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
