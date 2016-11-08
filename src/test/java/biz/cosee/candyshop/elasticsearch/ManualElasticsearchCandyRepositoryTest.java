package biz.cosee.candyshop.elasticsearch;

import biz.cosee.candyshop.domain.Candy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ManualElasticsearchCandyRepositoryTest {
    @Autowired
    private ManualElasticsearchCandyRepository manualElasticsearchCandyRepository;
    @Autowired
    private Client client;
    @Autowired
    private CandyRepository candyRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void initializeElasticsearchIndex() throws JsonProcessingException {
        //client.admin().indices().delete(new DeleteIndexRequest("candies")).actionGet();

        client.admin().indices().create(new CreateIndexRequest("candies")).actionGet();

        Candy candy = Candy.builder()
                .id(2L)
                .name("Snickers")
                .brand("Mars")
                .price(150)
                .rating(1)
                .calories(299)
                .build();
        client.index(new IndexRequest("candies", "candy", "2").source(mapToJSON(candy))).actionGet();

        Candy candy2 = Candy.builder()
                .id(6L)
                .name("Kitkat")
                .brand("Nestle")
                .price(200)
                .rating(3)
                .calories(217)
                .build();
        client.index(new IndexRequest("candies", "candy", "6").source(mapToJSON(candy2))).actionGet();
        client.admin().indices().prepareRefresh("candies").get();
    }

    @After
    public void tearDown() {
        client.admin().indices().delete(new DeleteIndexRequest("candies")).actionGet();
    }

    public String mapToJSON(Candy candy) throws JsonProcessingException {
        return objectMapper.writer().writeValueAsString(candy);
    }

    @Test
    public void findPriceBetween() {
        List<Candy> findings = manualElasticsearchCandyRepository.findByPriceBetween(100, 180);

        assertThat(findings.size()).isEqualTo(1);
        assertThat(findings.get(0).getName()).isEqualTo("Snickers");

    }

    @Test
    public void findCandyByBrand() throws Exception {
        List<Candy> findings = manualElasticsearchCandyRepository.findCandyByBrand("Mars");

        assertThat(findings.size()).isEqualTo(1);
    }
}