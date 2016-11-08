package biz.cosee.candyshop.jpa;

import biz.cosee.candyshop.domain.Candy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CandyJPARepositoryTest {

    @Autowired
    CandyJPARepository jpaRepository;

    @Test
    public void testSimpleStorage() {
        Candy candy = Candy.builder()
                .id(1L)
                .name("Twix")
                .brand("Mars")
                .price(100)
                .rating(4)
                .calories(283)
                .build();

        jpaRepository.save(candy);

        Candy found = jpaRepository.findOne(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Twix");
    }

}