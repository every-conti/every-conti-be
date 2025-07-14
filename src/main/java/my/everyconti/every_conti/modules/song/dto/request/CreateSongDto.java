package my.everyconti.every_conti.modules.song.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import my.everyconti.every_conti.constant.song.SongTempo;
import my.everyconti.every_conti.constant.song.SongType;

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
    @Size(min = 5, max = 150, message = "곡 이름은 5글자 이상, 150글자 이하로 입력 해야 합니다.")
    private String songName;

    @Size(max = 5000, message = "가사는 5000글자 이하로 입력 해야 합니다.")
    private String lyrics;

    @NotBlank
    @Size(max = 150, message = "유튜브 링크는 150글자 이하로 입력 해야 합니다.")
    private String reference;

    @NotNull(message = "곡 종류는 필수입니다.")
    private SongType songType;

    @NotNull(message = "등록자 아이디는 필수입니다.")
    private String creatorId;

    @NotNull(message = "찬양팀 아이디는 필수입니다.")
    private String praiseTeamId;

    @NotEmpty(message = "themeIds는 비어 있을 수 없습니다.")
    private List<String> themeIds;;

    /*
    --------Sub columns--------
     */
    private SongTempo tempo;

    private String seasonId;

    private String bibleBook;

    private Integer bibleChapter;

    private Integer bibleVerse;

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
