package tobacco.jpa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpecificationWithProductDto {
    private Long productId;
    private String productTitle;
    private String specificationTitle;
    private String value;
}