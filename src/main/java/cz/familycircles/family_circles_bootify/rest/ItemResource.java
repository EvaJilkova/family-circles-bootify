package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.ItemDTO;
import cz.familycircles.family_circles_bootify.service.ItemService;
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
@RequestMapping(value = "/api/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemResource {

    private final ItemService itemService;

    public ItemResource(final ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable(name = "itemId") final UUID itemId) {
        return ResponseEntity.ok(itemService.get(itemId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createItem(@RequestBody @Valid final ItemDTO itemDTO) {
        final UUID createdItemId = itemService.create(itemDTO);
        return new ResponseEntity<>(createdItemId, HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<UUID> updateItem(@PathVariable(name = "itemId") final UUID itemId,
            @RequestBody @Valid final ItemDTO itemDTO) {
        itemService.update(itemId, itemDTO);
        return ResponseEntity.ok(itemId);
    }

    @DeleteMapping("/{itemId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteItem(@PathVariable(name = "itemId") final UUID itemId) {
        final ReferencedWarning referencedWarning = itemService.getReferencedWarning(itemId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        itemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }

}
