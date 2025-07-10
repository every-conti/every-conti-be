package my.everyconti.every_conti.modules.auth.domain;


import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.constant.role.RoleType;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.domain.MemberRole;

import java.util.Collection;
import java.util.Set;

@Entity
//@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleType roleName;

//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<MemberRole> memberRoles;
    @OneToMany(mappedBy = "role")
    private Collection<MemberRole> memberRole;

    public Collection<MemberRole> getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Collection<MemberRole> memberRole) {
        this.memberRole = memberRole;
    }
}