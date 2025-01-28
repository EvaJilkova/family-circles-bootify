package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.RegionDTO;
import cz.familycircles.family_circles_bootify.service.RegionService;
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
@RequestMapping(value = "/api/regions", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegionResource {

    private final RegionService regionService;

    public RegionResource(final RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        return ResponseEntity.ok(regionService.findAll());
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionDTO> getRegion(
            @PathVariable(name = "regionId") final UUID regionId) {
        return ResponseEntity.ok(regionService.get(regionId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createRegion(@RequestBody @Valid final RegionDTO regionDTO) {
        final UUID createdRegionId = regionService.create(regionDTO);
        return new ResponseEntity<>(createdRegionId, HttpStatus.CREATED);
    }

    @PutMapping("/{regionId}")
    public ResponseEntity<UUID> updateRegion(@PathVariable(name = "regionId") final UUID regionId,
            @RequestBody @Valid final RegionDTO regionDTO) {
        regionService.update(regionId, regionDTO);
        return ResponseEntity.ok(regionId);
    }

    @DeleteMapping("/{regionId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRegion(@PathVariable(name = "regionId") final UUID regionId) {
        final ReferencedWarning referencedWarning = regionService.getReferencedWarning(regionId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        regionService.delete(regionId);
        return ResponseEntity.noContent().build();
    }

}
