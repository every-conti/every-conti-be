package my.everyconti.every_conti.modules.auth.domain;


import jakarta.persistence.*;
import lombok.*;
import my.everyconti.every_conti.constant.role.RoleType;
import my.everyconti.every_conti.modules.member.domain.MemberRole;

import java.util.Collection;

@Entity
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

    @OneToMany(mappedBy = "role")
    private Collection<MemberRole> memberRole;
}