package biz.cosee.candyshop.jpa;

import biz.cosee.candyshop.domain.Candy;
import org.springframework.data.repository.CrudRepository;

public interface CandyJPARepository extends CrudRepository<Candy, Long> {
}
