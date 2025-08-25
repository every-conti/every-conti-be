package com.everyconti.every_conti.modules.song.domain.es;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "songs")
@Setting(settingPath = "/elasticsearch/song-full-text-settings.json")  // 설정 파일 경로
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String songName;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String lyrics;
}