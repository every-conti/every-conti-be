package com.everyconti.every_conti.modules.song.dto.response;

import lombok.*;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.song.domain.PraiseTeam;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PraiseTeamDto {
    private String id;

    private String praiseTeamName;

    private String previewImg;

    public PraiseTeamDto(PraiseTeam praiseTeam, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(praiseTeam.getId());
        praiseTeamName = praiseTeam.getPraiseTeamName();
        previewImg = praiseTeam.getPreviewImg();
    }

    @Override
    public String toString() {
        return "PraiseTeamDto{" +
                "id='" + id + '\'' +
                ", praiseTeamName='" + praiseTeamName + '\'' +
                ", previewImg='" + previewImg + '\'' +
                '}';
    }
}