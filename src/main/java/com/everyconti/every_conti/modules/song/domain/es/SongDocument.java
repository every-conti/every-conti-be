package com.everyconti.every_conti.modules.song.domain.es;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "songs")
@Setting(settingPath = "elasticsearch/songs-full-text-settings.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String songName;
    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String lyrics;
}