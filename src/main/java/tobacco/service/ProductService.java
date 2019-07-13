package tobacco.service;

import tobacco.service.model.ProductSnapshot;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<ProductSnapshot> getProductByTitle(String title);
    List<ProductSnapshot> getProducts(Long offset, Long limit);
}
