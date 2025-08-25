package com.everyconti.every_conti.common.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {

    private boolean success;
    private T data;

    @Override
    public String toString() {
        return "CommonResponseDto{" +
                "success='" + success + '\'' +
                ", data='" + data.toString() + '\'' +
                '}';
    }
}