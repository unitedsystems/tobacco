package tobacco.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobacco.jpa.model.Product;
import tobacco.jpa.model.ProductSpecification;
import tobacco.jpa.model.Specification;
import tobacco.jpa.model.dto.SpecificationDto;
import tobacco.jpa.model.dto.SpecificationWithProductDto;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void createData() {
        Specification cpuSpecification = new Specification("CPU");
        entityManager.persist(cpuSpecification);
        Specification memorySpecification = new Specification("Memory");
        entityManager.persist(memorySpecification);

        Product product = new Product("IPAD");
        entityManager.persist(product);
        entityManager.persist(new ProductSpecification(product, cpuSpecification, "1.2GHz"));
        entityManager.persist(new ProductSpecification(product, memorySpecification, "2Gb"));

        product = new Product("IPHONE");
        entityManager.persist(product);
        entityManager.persist(new ProductSpecification(product, cpuSpecification, "2.4GHz"));
    }

    @Test
    void found_by_title() {
        Optional<Product> foundProduct = productRepository.findByTitle("IPAD");
        assertThat(foundProduct).isPresent();
    }

    @Test
    void not_found_by_title() {
        Optional<Product> foundProduct = productRepository.findByTitle("not-found");
        assertThat(foundProduct).isNotPresent();
    }

    @Test
    void fetch_product_by_title() {
        Product foundProduct = productRepository.findByTitle("IPAD").get();
        List<SpecificationDto> dtos = productRepository.fetchProductById(foundProduct.getId());

        Object[] titles = dtos.stream().map(SpecificationDto::getTitle).toArray();
        assertThat(titles).isEqualTo(Stream.of("CPU", "Memory").toArray());

        titles = dtos.stream().map(SpecificationDto::getValue).toArray();
        assertThat(titles).isEqualTo(Stream.of("1.2GHz", "2Gb").toArray());
    }

    @Test
    void fetch_page() {
        List<SpecificationWithProductDto> dtos = productRepository.fetchProductPage(1L, 1L);

        Object[] titles = dtos.stream().map(SpecificationWithProductDto::getSpecificationTitle).toArray();
        assertThat(titles).isEqualTo(Stream.of("CPU").toArray());

        titles = dtos.stream().map(SpecificationWithProductDto::getValue).toArray();
        assertThat(titles).isEqualTo(Stream.of("2.4GHz").toArray());

    }
}