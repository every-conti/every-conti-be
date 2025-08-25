package com.everyconti.every_conti.modules.song.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.everyconti.every_conti.constant.song.SongKey;
import com.everyconti.every_conti.constant.song.SongTempo;
import com.everyconti.every_conti.constant.song.SongType;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSongDto {

    /*
    --------Main columns--------
     */
    @NotBlank
    @Size(min = 2, max = 150, message = "곡 이름은 2글자 이상, 150글자 이하로 입력 해야 합니다.")
    private String songName;

    @Size(max = 5000, message = "가사는 5000글자 이하로 입력 해야 합니다.")
    private String lyrics;

    @NotBlank
    @Size(max = 150, message = "유튜브 링크는 150글자 이하로 입력 해야 합니다.")
    private String youtubeVId;

    @NotNull(message = "곡 종류는 필수입니다.")
    private SongType songType;

    @NotNull(message = "등록자 아이디는 필수입니다.")
    private String creatorId;

    @NotNull(message = "찬양팀 아이디는 필수입니다.")
    private String praiseTeamId;

    @NotNull(message = "썸네일은 필수입니다.")
    private String thumbnail;

    /*
    --------Sub columns--------
     */
    private List<String> themeIds;;

    private SongTempo tempo;

    private String seasonId;

    private SongKey key;

    private Integer duration;

    private String bibleId;

    private String bibleChapterId;

    private String bibleVerseId;
//
//    @Override
//    public String toString() {
//        return "signUpDto{" +
//                "nickname='" + nickname + '\'' +
//                ", email='" + email + '\'' +
//                ", church='" + church + '\'' +
//                '}';
//    }
}
