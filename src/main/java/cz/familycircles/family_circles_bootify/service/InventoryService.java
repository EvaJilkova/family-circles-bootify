package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Inventory;
import cz.familycircles.family_circles_bootify.domain.Item;
import cz.familycircles.family_circles_bootify.domain.User;
import cz.familycircles.family_circles_bootify.model.InventoryDTO;
import cz.familycircles.family_circles_bootify.repos.InventoryRepository;
import cz.familycircles.family_circles_bootify.repos.ItemRepository;
import cz.familycircles.family_circles_bootify.repos.UserRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public InventoryService(final InventoryRepository inventoryRepository,
            final ItemRepository itemRepository, final UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<InventoryDTO> findAll() {
        final List<Inventory> inventories = inventoryRepository.findAll(Sort.by("inventoryId"));
        return inventories.stream()
                .map(inventory -> mapToDTO(inventory, new InventoryDTO()))
                .toList();
    }

    public InventoryDTO get(final UUID inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .map(inventory -> mapToDTO(inventory, new InventoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final InventoryDTO inventoryDTO) {
        final Inventory inventory = new Inventory();
        mapToEntity(inventoryDTO, inventory);
        return inventoryRepository.save(inventory).getInventoryId();
    }

    public void update(final UUID inventoryId, final InventoryDTO inventoryDTO) {
        final Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(inventoryDTO, inventory);
        inventoryRepository.save(inventory);
    }

    public void delete(final UUID inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    private InventoryDTO mapToDTO(final Inventory inventory, final InventoryDTO inventoryDTO) {
        inventoryDTO.setInventoryId(inventory.getInventoryId());
        inventoryDTO.setItemId(inventory.getItemId() == null ? null : inventory.getItemId().getItemId());
        inventoryDTO.setUserId(inventory.getUserId() == null ? null : inventory.getUserId().getUserId());
        return inventoryDTO;
    }

    private Inventory mapToEntity(final InventoryDTO inventoryDTO, final Inventory inventory) {
        final Item itemId = inventoryDTO.getItemId() == null ? null : itemRepository.findById(inventoryDTO.getItemId())
                .orElseThrow(() -> new NotFoundException("itemId not found"));
        inventory.setItemId(itemId);
        final User userId = inventoryDTO.getUserId() == null ? null : userRepository.findById(inventoryDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        inventory.setUserId(userId);
        return inventory;
    }

}
