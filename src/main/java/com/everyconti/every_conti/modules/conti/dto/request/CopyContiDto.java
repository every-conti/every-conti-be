package com.everyconti.every_conti.modules.conti.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CopyContiDto {
    private String memberId;
    private String copiedContiId;
    private String targetContiId;
    private List<String> songIds;
}
