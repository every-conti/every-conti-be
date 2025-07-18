package my.everyconti.every_conti.modules.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "member_follow",
        uniqueConstraints = @UniqueConstraint(columnNames = {"following", "followed"}))
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFollow extends NowTimeForJpa {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private Member following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed")
    private Member followed;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
