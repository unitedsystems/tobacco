package tobacco.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tobacco.jpa.model.Product;
import tobacco.jpa.model.dto.SpecificationDto;
import tobacco.jpa.model.dto.SpecificationWithProductDto;
import tobacco.jpa.repository.ProductRepository;
import tobacco.service.model.ProductSnapshot;
import tobacco.service.model.SpecificationSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private

    @BeforeEach
    void createData() {
        MockitoAnnotations.initMocks(this);

        when(productRepository.findByTitle(any())).thenReturn(Optional.of(new Product("IPAD")));

        when(productRepository.fetchProductById(any())).thenReturn(
                List.of(
                        new SpecificationDto("CPU", "1.2GHz"),
                        new SpecificationDto("Memory", "2Gb")
                )
        );

        when(productRepository.fetchProductPage(any(), any())).thenReturn(
                List.of(
                        new SpecificationWithProductDto(1L, "IPAD", "CPU", "1.2GHz"),
                        new SpecificationWithProductDto(1L, "IPAD", "Memory", "2Gb"),
                        new SpecificationWithProductDto(2L, "IPHONE", "CPU", "2.4GHz")
                )
        );
    }

    @Test
    void get_product_by_id() {
        ProductSnapshot snapshot = productService.getProductByTitle("IPAD").get();
        List<SpecificationSnapshot> specList = new ArrayList<>();
        specList.add(new SpecificationSnapshot("CPU", "1.2GHz"));
        specList.add(new SpecificationSnapshot("Memory", "2Gb"));

        assertThat(snapshot.getTitle()).isEqualTo("IPAD");
        assertThat(snapshot.getSpecifications()).isEqualTo(specList);
    }

    @Test
    void get_product_page() {
        List<ProductSnapshot> snapshots = productService.getProducts(0L,100L);

        List<ProductSnapshot> productList = new ArrayList<>();
        ProductSnapshot ps = new ProductSnapshot( "IPAD");
        ps.addSpecification(new SpecificationSnapshot("CPU", "1.2GHz"));
        ps.addSpecification(new SpecificationSnapshot("Memory", "2Gb"));
        productList.add(ps);

        ps = new ProductSnapshot( "IPHONE");
        ps.addSpecification(new SpecificationSnapshot("CPU", "2.4GHz"));
        productList.add(ps);

        assertThat(snapshots).isEqualTo(productList);
    }
}