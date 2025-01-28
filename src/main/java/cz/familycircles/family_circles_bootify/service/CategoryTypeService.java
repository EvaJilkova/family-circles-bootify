package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.CategoryType;
import cz.familycircles.family_circles_bootify.model.CategoryTypeDTO;
import cz.familycircles.family_circles_bootify.repos.CategoryTypeRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryTypeService {

    private final CategoryTypeRepository categoryTypeRepository;

    public CategoryTypeService(final CategoryTypeRepository categoryTypeRepository) {
        this.categoryTypeRepository = categoryTypeRepository;
    }

    public List<CategoryTypeDTO> findAll() {
        final List<CategoryType> categoryTypes = categoryTypeRepository.findAll(Sort.by("categoryTypeId"));
        return categoryTypes.stream()
                .map(categoryType -> mapToDTO(categoryType, new CategoryTypeDTO()))
                .toList();
    }

    public CategoryTypeDTO get(final UUID categoryTypeId) {
        return categoryTypeRepository.findById(categoryTypeId)
                .map(categoryType -> mapToDTO(categoryType, new CategoryTypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CategoryTypeDTO categoryTypeDTO) {
        final CategoryType categoryType = new CategoryType();
        mapToEntity(categoryTypeDTO, categoryType);
        return categoryTypeRepository.save(categoryType).getCategoryTypeId();
    }

    public void update(final UUID categoryTypeId, final CategoryTypeDTO categoryTypeDTO) {
        final CategoryType categoryType = categoryTypeRepository.findById(categoryTypeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryTypeDTO, categoryType);
        categoryTypeRepository.save(categoryType);
    }

    public void delete(final UUID categoryTypeId) {
        categoryTypeRepository.deleteById(categoryTypeId);
    }

    private CategoryTypeDTO mapToDTO(final CategoryType categoryType,
            final CategoryTypeDTO categoryTypeDTO) {
        categoryTypeDTO.setCategoryTypeId(categoryType.getCategoryTypeId());
        categoryTypeDTO.setName(categoryType.getName());
        return categoryTypeDTO;
    }

    private CategoryType mapToEntity(final CategoryTypeDTO categoryTypeDTO,
            final CategoryType categoryType) {
        categoryType.setName(categoryTypeDTO.getName());
        return categoryType;
    }

    public boolean nameExists(final String name) {
        return categoryTypeRepository.existsByNameIgnoreCase(name);
    }

}
