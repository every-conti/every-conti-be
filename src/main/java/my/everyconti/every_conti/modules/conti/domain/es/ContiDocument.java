package my.everyconti.every_conti.modules.conti.domain.es;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "contis")
@Setting(settingPath = "/elasticsearch/full-text-settings.json")  // 설정 파일 경로
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContiDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String contiName;
}