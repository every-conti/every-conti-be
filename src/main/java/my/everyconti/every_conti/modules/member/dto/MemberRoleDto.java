package my.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import my.everyconti.every_conti.constant.role.RoleType;
import my.everyconti.every_conti.modules.member.domain.MemberRole;

@Getter
public class MemberRoleDto {
    private RoleType roleName;

    public MemberRoleDto(MemberRole mr) {
        this.roleName = mr.getRole().getRoleName();
    }

    @Override
    public String toString(){
        return "memberRoleDto{" +
                "roleName='" + getRoleName() + '\'' +
                '}';
    }
}
