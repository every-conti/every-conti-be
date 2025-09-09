package com.everyconti.every_conti.modules.conti.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateContiDto {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private LocalDate date;

    private String memberId;

    private List<String> songIds;
}
