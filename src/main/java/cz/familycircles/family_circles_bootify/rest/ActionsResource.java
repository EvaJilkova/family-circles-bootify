package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.ActionsDTO;
import cz.familycircles.family_circles_bootify.service.ActionsService;
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
@RequestMapping(value = "/api/actionss", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActionsResource {

    private final ActionsService actionsService;

    public ActionsResource(final ActionsService actionsService) {
        this.actionsService = actionsService;
    }

    @GetMapping
    public ResponseEntity<List<ActionsDTO>> getAllActionss() {
        return ResponseEntity.ok(actionsService.findAll());
    }

    @GetMapping("/{actionsId}")
    public ResponseEntity<ActionsDTO> getActions(
            @PathVariable(name = "actionsId") final UUID actionsId) {
        return ResponseEntity.ok(actionsService.get(actionsId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createActions(@RequestBody @Valid final ActionsDTO actionsDTO) {
        final UUID createdActionsId = actionsService.create(actionsDTO);
        return new ResponseEntity<>(createdActionsId, HttpStatus.CREATED);
    }

    @PutMapping("/{actionsId}")
    public ResponseEntity<UUID> updateActions(
            @PathVariable(name = "actionsId") final UUID actionsId,
            @RequestBody @Valid final ActionsDTO actionsDTO) {
        actionsService.update(actionsId, actionsDTO);
        return ResponseEntity.ok(actionsId);
    }

    @DeleteMapping("/{actionsId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteActions(
            @PathVariable(name = "actionsId") final UUID actionsId) {
        actionsService.delete(actionsId);
        return ResponseEntity.noContent().build();
    }

}
