package my.everyconti.every_conti.member.domain;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String church;

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
