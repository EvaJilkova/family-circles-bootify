package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Category;
import cz.familycircles.family_circles_bootify.domain.Item;
import cz.familycircles.family_circles_bootify.model.CategoryDTO;
import cz.familycircles.family_circles_bootify.repos.CategoryRepository;
import cz.familycircles.family_circles_bootify.repos.ItemRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("categoryId"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getCategoryId();
    }

    public void update(final UUID categoryId, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }

    public boolean categoryNameExists(final String categoryName) {
        return categoryRepository.existsByCategoryNameIgnoreCase(categoryName);
    }

    public ReferencedWarning getReferencedWarning(final UUID categoryId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundException::new);
        final Item categoryIdItem = itemRepository.findFirstByCategoryId(category);
        if (categoryIdItem != null) {
            referencedWarning.setKey("category.item.categoryId.referenced");
            referencedWarning.addParam(categoryIdItem.getItemId());
            return referencedWarning;
        }
        return null;
    }

}
