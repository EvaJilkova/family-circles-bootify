package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.ActionDTO;
import cz.familycircles.family_circles_bootify.service.ActionService;
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
@RequestMapping(value = "/api/actions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActionResource {

    private final ActionService actionService;

    public ActionResource(final ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public ResponseEntity<List<ActionDTO>> getAllActions() {
        return ResponseEntity.ok(actionService.findAll());
    }

    @GetMapping("/{actionId}")
    public ResponseEntity<ActionDTO> getAction(
            @PathVariable(name = "actionId") final UUID actionId) {
        return ResponseEntity.ok(actionService.get(actionId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createAction(@RequestBody @Valid final ActionDTO actionDTO) {
        final UUID createdActionId = actionService.create(actionDTO);
        return new ResponseEntity<>(createdActionId, HttpStatus.CREATED);
    }

    @PutMapping("/{actionId}")
    public ResponseEntity<UUID> updateAction(@PathVariable(name = "actionId") final UUID actionId,
            @RequestBody @Valid final ActionDTO actionDTO) {
        actionService.update(actionId, actionDTO);
        return ResponseEntity.ok(actionId);
    }

    @DeleteMapping("/{actionId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAction(@PathVariable(name = "actionId") final UUID actionId) {
        final ReferencedWarning referencedWarning = actionService.getReferencedWarning(actionId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        actionService.delete(actionId);
        return ResponseEntity.noContent().build();
    }

}
