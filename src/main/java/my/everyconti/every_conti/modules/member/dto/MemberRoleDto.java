package my.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import my.everyconti.every_conti.constant.role.RoleType;
import my.everyconti.every_conti.modules.auth.domain.Role;
import my.everyconti.every_conti.modules.member.domain.MemberRole;

@Getter
public class MemberRoleDto {
    private RoleType roleName;

    public MemberRoleDto(MemberRole mr) {
        this.roleName = mr.getRole().getRoleName(); // 여기서 Lazy 로딩 발생
    }
}
