package my.everyconti.every_conti.modules.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Member extends NowTimeForJpa {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String church;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @Column(nullable = false)
    private String password;

    public Member() {}

    public Member(String nickname, String email, String church, String encodedPassword) {
        this.nickname = nickname;
        this.email = email;
        this.church = church;
        this.password = encodedPassword;
    }
}
