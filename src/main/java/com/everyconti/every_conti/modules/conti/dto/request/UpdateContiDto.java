package com.everyconti.every_conti.modules.conti.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UpdateContiDto {
    private String title;
    private String description;
    private LocalDate date;

    private List<String> songIds;
    private String memberId;
}
