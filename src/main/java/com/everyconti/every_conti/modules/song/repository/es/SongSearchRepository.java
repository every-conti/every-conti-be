package com.everyconti.every_conti.modules.song.repository.es;

import com.everyconti.every_conti.modules.song.domain.es.SongDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongSearchRepository extends ElasticsearchRepository<SongDocument, Long> {

    @Query("""
    {
      "bool": {
        "should": [
          { "match_phrase":       { "songName": { "query": "?0", "slop": 0, "boost": 15.0 } } },
          { "match_phrase_prefix":{ "songName": { "query": "?0", "max_expansions": 10, "boost": 8.0 } } },
          { "match":              { "songName": { "query": "?0", "operator": "and", "boost": 5.0 } } },
          { "match":              { "lyrics":   { "query": "?0", "operator": "and", "boost": 1.0 } } }
        ],
        "minimum_should_match": 1
      }
    }
    """)
    Page<SongDocument> fullTextSearch(String query, Pageable pageable);
}