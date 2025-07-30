package my.everyconti.every_conti.modules.song.repository.es;

import my.everyconti.every_conti.modules.song.domain.es.SongDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SongSearchRepository extends ElasticsearchRepository<SongDocument, Long> {

    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"songName\": \"?0\"}}," +
            "{\"match\": {\"lyrics\": \"?0\"}}" +
            "]}}")
    List<SongDocument> fullTextSearch(String query);
}