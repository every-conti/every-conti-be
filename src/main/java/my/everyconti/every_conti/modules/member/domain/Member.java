package my.everyconti.every_conti.modules.member.domain;

import jakarta.persistence.*;
import my.everyconti.every_conti.common.entity.NowTimeForJpa;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Member extends NowTimeForJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String church;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Member() {}

    public Member(String nickname, String email, String church) {
        this.nickname = nickname;
        this.email = email;
        this.church = church;
    }

    public Long getId() {
        return id;
    }
    public String getEmail(){
        return email;
    };
}
