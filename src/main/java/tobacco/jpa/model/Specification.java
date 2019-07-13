package tobacco.jpa.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="specifications")
public class Specification {
    @Id
    @GeneratedValue
    Long id;

    @Size(min = 1)
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(
        mappedBy = "specification",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ProductSpecification> productSpecifications = new HashSet<>();

    public Specification(String title) {
        this.title = title;
    }
}
