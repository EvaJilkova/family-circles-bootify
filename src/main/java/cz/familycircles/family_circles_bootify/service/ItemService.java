package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Category;
import cz.familycircles.family_circles_bootify.domain.Inventory;
import cz.familycircles.family_circles_bootify.domain.Item;
import cz.familycircles.family_circles_bootify.model.ItemDTO;
import cz.familycircles.family_circles_bootify.repos.CategoryRepository;
import cz.familycircles.family_circles_bootify.repos.InventoryRepository;
import cz.familycircles.family_circles_bootify.repos.ItemRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    public ItemService(final ItemRepository itemRepository,
            final CategoryRepository categoryRepository,
            final InventoryRepository inventoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<ItemDTO> findAll() {
        final List<Item> items = itemRepository.findAll(Sort.by("itemId"));
        return items.stream()
                .map(item -> mapToDTO(item, new ItemDTO()))
                .toList();
    }

    public ItemDTO get(final UUID itemId) {
        return itemRepository.findById(itemId)
                .map(item -> mapToDTO(item, new ItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ItemDTO itemDTO) {
        final Item item = new Item();
        mapToEntity(itemDTO, item);
        return itemRepository.save(item).getItemId();
    }

    public void update(final UUID itemId, final ItemDTO itemDTO) {
        final Item item = itemRepository.findById(itemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(itemDTO, item);
        itemRepository.save(item);
    }

    public void delete(final UUID itemId) {
        itemRepository.deleteById(itemId);
    }

    private ItemDTO mapToDTO(final Item item, final ItemDTO itemDTO) {
        itemDTO.setItemId(item.getItemId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setCategoryId(item.getCategoryId() == null ? null : item.getCategoryId().getCategoryId());
        return itemDTO;
    }

    private Item mapToEntity(final ItemDTO itemDTO, final Item item) {
        item.setItemName(itemDTO.getItemName());
        final Category categoryId = itemDTO.getCategoryId() == null ? null : categoryRepository.findById(itemDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("categoryId not found"));
        item.setCategoryId(categoryId);
        return item;
    }

    public boolean itemNameExists(final String itemName) {
        return itemRepository.existsByItemNameIgnoreCase(itemName);
    }

    public ReferencedWarning getReferencedWarning(final UUID itemId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Item item = itemRepository.findById(itemId)
                .orElseThrow(NotFoundException::new);
        final Inventory itemIdInventory = inventoryRepository.findFirstByItemId(item);
        if (itemIdInventory != null) {
            referencedWarning.setKey("item.inventory.itemId.referenced");
            referencedWarning.addParam(itemIdInventory.getInventoryId());
            return referencedWarning;
        }
        return null;
    }

}
