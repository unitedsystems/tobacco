package tobacco.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductSnapshot {
    @NonNull
    private String title;
    private List<SpecificationSnapshot> specifications = new ArrayList<>();

    public void addSpecification(SpecificationSnapshot specification) {
        specifications.add(specification);
    }
}
