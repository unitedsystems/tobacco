package tobacco.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import tobacco.service.ProductService;
import tobacco.service.model.ProductSnapshot;
import tobacco.service.model.SpecificationSnapshot;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @MockBean
    ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private ProductSnapshot product;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        product = new ProductSnapshot("IPAD");
        product.addSpecification(new SpecificationSnapshot("CPU", "1.2GHz"));

        when(productService.getProducts(anyLong(), anyLong())).thenReturn(List.of(product));
    }

    @Test
    void product_found() throws Exception {
        when(productService.getProductByTitle(anyString())).thenReturn(Optional.of(product));
        mockMvc.perform(
                get("/product").param("title", "IPAD"))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(product)));

    }

    @Test
    void product_not_found() throws Exception {
        when(productService.getProductByTitle(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(
                get("/product").param("title", "IPAD"))
                .andExpect(status().isNotFound());
    }

    @Test
    void fetch_all_products() throws Exception {
        mockMvc.perform(
                get("/products")
                        .param("offset", "0")
                        .param("limit", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(product))));
    }
}