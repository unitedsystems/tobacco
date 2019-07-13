package tobacco.jpa.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "products_title_index", columnList = "title", unique = true)
        }
)
public class Product {
    @Id
    @GeneratedValue
    Long id;

    @Size(min = 1)
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProductSpecification> productSpecifications = new HashSet<>();

    public Product(String title) {
        this.title = title;
    }
}
