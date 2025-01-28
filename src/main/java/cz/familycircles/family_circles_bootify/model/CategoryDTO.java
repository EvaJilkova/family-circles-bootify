package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDTO {

    private UUID categoryId;

    @NotNull
    @Size(max = 255)
    @CategoryCategoryNameUnique
    private String categoryName;

}
