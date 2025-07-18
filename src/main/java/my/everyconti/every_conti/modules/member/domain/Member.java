package my.everyconti.every_conti.modules.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import my.everyconti.every_conti.modules.song.domain.PraiseTeam;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends NowTimeForJpa {
    @JsonIgnore
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "church")
    private String church;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "activated", nullable = false)
    private boolean activated;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRole> memberRoles;

    @OneToOne
    @JoinColumn(name = "praise_team")
    private PraiseTeam praiseTeam;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberFollow> following;

}
