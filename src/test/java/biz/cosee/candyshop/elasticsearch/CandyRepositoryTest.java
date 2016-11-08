package biz.cosee.candyshop.elasticsearch;

import biz.cosee.candyshop.domain.Candy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CandyRepositoryTest {

    @Autowired
    CandyRepository candyRepository;

    @Test
    public void simpleStorageTest() {
        Candy candy = Candy.builder()
                .id(10L)
                .name("Twix")
                .brand("Mars")
                .price(100)
                .rating(4)
                .calories(283)
                .build();

        candyRepository.save(candy);

        Candy found = candyRepository.findOne(10L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Twix");
    }

    @Test
    public void testCustomFindByMethod() {
        Candy candy = Candy.builder()
                .id(20L)
                .name("Snickers")
                .brand("Mars")
                .price(150)
                .rating(1)
                .calories(299)
                .build();
        candyRepository.save(candy);

        List<Candy> findings = candyRepository.findByBrand("Mars");

        assertThat(findings.size()).isEqualTo(20);
        assertThat(findings).contains(candy);
    }

    @Test
    public void findPriceBetween() {
        candyRepository.save(Candy.builder()
                .id(21L)
                .name("Snickers")
                .brand("Mars")
                .price(150)
                .rating(1)
                .calories(299)
                .build());
        candyRepository.save(Candy.builder()
                .id(11L)
                .name("Twix")
                .brand("Mars")
                .price(100)
                .rating(4)
                .calories(283)
                .build());

        List<Candy> findings = candyRepository.findByPriceBetween(120, 200);

        assertThat(findings.size()).isEqualTo(1);
        assertThat(findings.get(0).getName()).isEqualTo("Snickers");
    }

}