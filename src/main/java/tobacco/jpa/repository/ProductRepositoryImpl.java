package tobacco.jpa.repository;

import org.springframework.stereotype.Repository;
import tobacco.jpa.model.Product;
import tobacco.jpa.model.dto.SpecificationDto;
import tobacco.jpa.model.dto.SpecificationWithProductDto;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final EntityManager em;

    public ProductRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public Optional<Product> findByTitle(String title) {
        Product product;
        try {
            product = em.createQuery(
                    "from Product WHERE title=:title", Product.class
            )
                    .setParameter("title", title)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return Optional.empty();
        }
        return Optional.of(product);

    }

    public List<SpecificationDto> fetchProductById(Long id) {
        @SuppressWarnings("unchecked")
        List<SpecificationDto> results = em.createQuery(
                "SELECT new tobacco.jpa.model.dto.SpecificationDto(s.title, ps.value) FROM ProductSpecification ps"
                        + " LEFT JOIN ps.specification s "
                        + " WHERE ps.id.productId = :id")
                .setParameter("id", id)
                .getResultList();
        return results;

    }

    public List<SpecificationWithProductDto> fetchProductPage(Long offset, Long limit) {
        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createNativeQuery(
                "SELECT p.id, p.title productTitle, s.title specificationTitle, ps.value "
                        + " FROM (SELECT * FROM products LIMIT :limit OFFSET :offset) p "
                        + " LEFT JOIN product_specifications ps ON p.id = ps.product_id "
                        + " LEFT JOIN specifications s ON s.id = ps.specification_id ")
                .setParameter("offset", offset)
                .setParameter("limit", limit)
                .getResultList();

        return results.stream().map((record) ->
                new SpecificationWithProductDto(
                        ((BigInteger) record[0]).longValue(),
                        (String) record[1],
                        (String) record[2],
                        (String) record[3]
                )
        ).collect(Collectors.toList());
    }
}
