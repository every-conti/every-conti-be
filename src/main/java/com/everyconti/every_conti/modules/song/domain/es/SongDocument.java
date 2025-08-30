package com.everyconti.every_conti.modules.song.domain.es;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "songs")
@Setting(settingPath = "elasticsearch/songs-full-text-settings.json")
public class SongDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori_index", searchAnalyzer = "nori_search")
    private String songName;

    @Field(type = FieldType.Text, analyzer = "nori_index", searchAnalyzer = "nori_search")
    private String lyrics;
}