package tobacco.jpa.repository;

import org.springframework.data.repository.Repository;
import tobacco.jpa.model.Product;
import tobacco.jpa.model.dto.SpecificationDto;
import tobacco.jpa.model.dto.SpecificationWithProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {
    Optional<Product> findByTitle(String title);

    List<SpecificationDto> fetchProductById(Long id);

    List<SpecificationWithProductDto> fetchProductPage(Long offset, Long limit);
}
