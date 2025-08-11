package my.everyconti.every_conti.modules.conti.repository.es;

import my.everyconti.every_conti.modules.conti.domain.es.ContiDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ContiSearchRepository extends ElasticsearchRepository<ContiDocument, Long> {

    @Query("{\"bool\": {\"should\": [" +
            "{\"match\": {\"contiName\": \"?0\"}}," +
            "]}}")
    List<ContiDocument> fullTextSearch(String query);
}