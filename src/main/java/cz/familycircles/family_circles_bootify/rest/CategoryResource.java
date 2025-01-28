package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.CategoryDTO;
import cz.familycircles.family_circles_bootify.service.CategoryService;
import cz.familycircles.family_circles_bootify.util.ReferencedException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(
            @PathVariable(name = "categoryId") final UUID categoryId) {
        return ResponseEntity.ok(categoryService.get(categoryId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCategory(@RequestBody @Valid final CategoryDTO categoryDTO) {
        final UUID createdCategoryId = categoryService.create(categoryDTO);
        return new ResponseEntity<>(createdCategoryId, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<UUID> updateCategory(
            @PathVariable(name = "categoryId") final UUID categoryId,
            @RequestBody @Valid final CategoryDTO categoryDTO) {
        categoryService.update(categoryId, categoryDTO);
        return ResponseEntity.ok(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable(name = "categoryId") final UUID categoryId) {
        final ReferencedWarning referencedWarning = categoryService.getReferencedWarning(categoryId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

}
