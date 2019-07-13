package tobacco.service;

import org.springframework.stereotype.Service;
import tobacco.jpa.model.Product;
import tobacco.jpa.model.dto.SpecificationDto;
import tobacco.jpa.model.dto.SpecificationWithProductDto;
import tobacco.jpa.repository.ProductRepository;
import tobacco.service.model.ProductSnapshot;
import tobacco.service.model.SpecificationSnapshot;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductSnapshot> getProductByTitle(String title) {
        Optional<Product> productOptional = productRepository.findByTitle(title);

        ProductSnapshot snapshot = null;
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<SpecificationDto> specificationDtos = productRepository.fetchProductById(product.getId());
            List<SpecificationSnapshot> specifications = specificationDtos.stream().map(
                    dto -> new SpecificationSnapshot(dto.getTitle(), dto.getValue())
            ).collect(Collectors.toList());
            snapshot = new ProductSnapshot(product.getTitle(), specifications);
        }
        return Optional.of(snapshot);
    }

    @Override
    public List<ProductSnapshot> getProducts(Long offset, Long limit) {
        List<SpecificationWithProductDto> dtos = productRepository.fetchProductPage(offset, limit);
        Map<Long, ProductSnapshot> map = new HashMap<>();
        for (SpecificationWithProductDto dto : dtos) {
            if (!map.containsKey(dto.getProductId())) {
                map.put(dto.getProductId(), new ProductSnapshot(dto.getProductTitle()));
            }
            map.get(dto.getProductId()).addSpecification(new SpecificationSnapshot(dto.getSpecificationTitle(), dto.getValue()));
        }
        return new ArrayList<>(map.values());
    }

}
