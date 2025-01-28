package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.CategoryTypeDTO;
import cz.familycircles.family_circles_bootify.service.CategoryTypeService;
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
@RequestMapping(value = "/api/categoryTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryTypeResource {

    private final CategoryTypeService categoryTypeService;

    public CategoryTypeResource(final CategoryTypeService categoryTypeService) {
        this.categoryTypeService = categoryTypeService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryTypeDTO>> getAllCategoryTypes() {
        return ResponseEntity.ok(categoryTypeService.findAll());
    }

    @GetMapping("/{categoryTypeId}")
    public ResponseEntity<CategoryTypeDTO> getCategoryType(
            @PathVariable(name = "categoryTypeId") final UUID categoryTypeId) {
        return ResponseEntity.ok(categoryTypeService.get(categoryTypeId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCategoryType(
            @RequestBody @Valid final CategoryTypeDTO categoryTypeDTO) {
        final UUID createdCategoryTypeId = categoryTypeService.create(categoryTypeDTO);
        return new ResponseEntity<>(createdCategoryTypeId, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryTypeId}")
    public ResponseEntity<UUID> updateCategoryType(
            @PathVariable(name = "categoryTypeId") final UUID categoryTypeId,
            @RequestBody @Valid final CategoryTypeDTO categoryTypeDTO) {
        categoryTypeService.update(categoryTypeId, categoryTypeDTO);
        return ResponseEntity.ok(categoryTypeId);
    }

    @DeleteMapping("/{categoryTypeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCategoryType(
            @PathVariable(name = "categoryTypeId") final UUID categoryTypeId) {
        categoryTypeService.delete(categoryTypeId);
        return ResponseEntity.noContent().build();
    }

}
