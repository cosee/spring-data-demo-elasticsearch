package biz.cosee.candyshop.elasticsearch;

import biz.cosee.candyshop.domain.Candy;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CandyRepository extends CrudRepository<Candy, Long> {
    List<Candy> findByBrand(String brand);


    List<Candy> findByPriceBetween(int low, int high);
}
