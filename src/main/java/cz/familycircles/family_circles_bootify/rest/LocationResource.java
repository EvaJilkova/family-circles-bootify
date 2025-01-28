package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.LocationDTO;
import cz.familycircles.family_circles_bootify.service.LocationService;
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
@RequestMapping(value = "/api/locations", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationResource {

    private final LocationService locationService;

    public LocationResource(final LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDTO> getLocation(
            @PathVariable(name = "locationId") final UUID locationId) {
        return ResponseEntity.ok(locationService.get(locationId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createLocation(@RequestBody @Valid final LocationDTO locationDTO) {
        final UUID createdLocationId = locationService.create(locationDTO);
        return new ResponseEntity<>(createdLocationId, HttpStatus.CREATED);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<UUID> updateLocation(
            @PathVariable(name = "locationId") final UUID locationId,
            @RequestBody @Valid final LocationDTO locationDTO) {
        locationService.update(locationId, locationDTO);
        return ResponseEntity.ok(locationId);
    }

    @DeleteMapping("/{locationId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable(name = "locationId") final UUID locationId) {
        final ReferencedWarning referencedWarning = locationService.getReferencedWarning(locationId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        locationService.delete(locationId);
        return ResponseEntity.noContent().build();
    }

}
