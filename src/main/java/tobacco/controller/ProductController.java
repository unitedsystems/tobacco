package tobacco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tobacco.service.model.ProductSnapshot;
import tobacco.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController("/")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "product", produces = "application/json")
    public @ResponseBody
    ProductSnapshot getProduct(@RequestParam String title) {
        Optional<ProductSnapshot> product = productService.getProductByTitle(title);
        if (product.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return product.get();
    }

    @GetMapping(value = "products", produces = "application/json")
    public @ResponseBody
    List<ProductSnapshot> getProduct(@RequestParam Long offset, @RequestParam Long limit) {
        return productService.getProducts(offset, limit);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
    }
}
