package biz.cosee.candyshop.elasticsearch;

import biz.cosee.candyshop.domain.Candy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ManualElasticsearchCandyRepository {
    private static Logger log = LoggerFactory.getLogger(ManualElasticsearchCandyRepository.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    //Autowired fields
    private ElasticsearchTemplate elasticsearchTemplate;
    private TransportClient client;

    @Autowired
    public ManualElasticsearchCandyRepository(ElasticsearchTemplate elasticsearchTemplate, TransportClient client) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.client = client;
    }


    public List<Candy> findByPriceBetween(int low, int high) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("candies")
                .setTypes("candy")
                .setQuery(QueryBuilders.rangeQuery("price").from(low).to(high));

        SearchResponse searchResponse = searchRequestBuilder
                .execute()
                .actionGet();

        SearchHits hits = searchResponse.getHits();
        List<Candy> candies = StreamSupport.stream(hits.spliterator(), false).map(hit -> {
            try {
                return objectMapper.readValue(hit.getSourceAsString(), Candy.class);
            } catch (IOException e) {
                log.warn("Could not parse ElasticSearch hit into Product", e);
                return null;
            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return candies;

    }

    public List<Candy> findCandyByBrand(String brand) {
        //Client client = elasticsearchTemplate.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("candies")
                .setTypes("candy")
                .setQuery(QueryBuilders.matchQuery("brand", brand));

        SearchResponse searchResponse = searchRequestBuilder
                .execute()
                .actionGet();

        SearchHits hits = searchResponse.getHits();
        List<Candy> candies = StreamSupport.stream(hits.spliterator(), false).map(hit -> {
            try {
                return objectMapper.readValue(hit.getSourceRef().toBytes(), Candy.class);
            } catch (IOException e) {
                log.warn("Could not parse ElasticSearch hit into Product", e);
                return null;
            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return candies;
    }

    public void getSuggestionsForBrand(String brand) {
        CompletionSuggestionBuilder brandCompletionSuggester = new CompletionSuggestionBuilder("brandCompletionSuggester")
                .field("brand")
                .text(brand);

        elasticsearchTemplate.suggest(brandCompletionSuggester);
    }
}
