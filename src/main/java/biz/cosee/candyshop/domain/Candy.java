package biz.cosee.candyshop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
@Document(indexName = "candies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candy {

    @Id
    private long id;

    @Column(unique = true)
    private String name;

    private String brand;

    private int price;

    private int rating;

    private int calories;

    @Version
    private Long version;

}
