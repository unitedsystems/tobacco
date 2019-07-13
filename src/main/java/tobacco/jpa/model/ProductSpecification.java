package tobacco.jpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product_specifications")
public class ProductSpecification implements Serializable {
    @EmbeddedId
    private ProductSpecificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("specificationId")
    private Specification specification;

    @Size(min=1)
    @Column(name = "value", nullable=false)
    private String value;

    public ProductSpecification(Product product, Specification specification, String value) {
        this.product = product;
        this.specification = specification;
        this.value = value;
        this.id = new ProductSpecificationId(product.getId(), specification.getId());
    }
}
