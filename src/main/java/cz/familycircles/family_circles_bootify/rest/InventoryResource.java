package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.InventoryDTO;
import cz.familycircles.family_circles_bootify.service.InventoryService;
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
@RequestMapping(value = "/api/inventories", produces = MediaType.APPLICATION_JSON_VALUE)
public class InventoryResource {

    private final InventoryService inventoryService;

    public InventoryResource(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDTO> getInventory(
            @PathVariable(name = "inventoryId") final UUID inventoryId) {
        return ResponseEntity.ok(inventoryService.get(inventoryId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createInventory(
            @RequestBody @Valid final InventoryDTO inventoryDTO) {
        final UUID createdInventoryId = inventoryService.create(inventoryDTO);
        return new ResponseEntity<>(createdInventoryId, HttpStatus.CREATED);
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<UUID> updateInventory(
            @PathVariable(name = "inventoryId") final UUID inventoryId,
            @RequestBody @Valid final InventoryDTO inventoryDTO) {
        inventoryService.update(inventoryId, inventoryDTO);
        return ResponseEntity.ok(inventoryId);
    }

    @DeleteMapping("/{inventoryId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable(name = "inventoryId") final UUID inventoryId) {
        inventoryService.delete(inventoryId);
        return ResponseEntity.noContent().build();
    }

}
