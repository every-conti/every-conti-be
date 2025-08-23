package my.everyconti.every_conti.modules.conti.repository.es;

import my.everyconti.every_conti.modules.conti.domain.es.ContiDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContiSearchRepository extends ElasticsearchRepository<ContiDocument, Long> {

    @Query("""
    {
      "bool": {
        "should": [
          { "match_phrase":        { "contiName": { "query": "?0", "slop": 0, "boost": 15.0 } } },
          { "match_phrase_prefix": { "contiName": { "query": "?0", "max_expansions": 10, "boost": 8.0 } } },
          { "match":               { "contiName": { "query": "?0", "operator": "and", "boost": 3.0 } } }
        ],
        "minimum_should_match": 1
      }
    }
    """)
    Page<ContiDocument> fullTextSearch(String query, Pageable pageable);
}