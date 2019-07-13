package tobacco.jpa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpecificationDto {
    private String title;
    private String value;
}
