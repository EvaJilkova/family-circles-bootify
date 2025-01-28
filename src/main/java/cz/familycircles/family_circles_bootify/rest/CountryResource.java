package cz.familycircles.family_circles_bootify.rest;

import cz.familycircles.family_circles_bootify.model.CountryDTO;
import cz.familycircles.family_circles_bootify.service.CountryService;
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
@RequestMapping(value = "/api/countries", produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryResource {

    private final CountryService countryService;

    public CountryResource(final CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryDTO> getCountry(
            @PathVariable(name = "countryId") final UUID countryId) {
        return ResponseEntity.ok(countryService.get(countryId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCountry(@RequestBody @Valid final CountryDTO countryDTO) {
        final UUID createdCountryId = countryService.create(countryDTO);
        return new ResponseEntity<>(createdCountryId, HttpStatus.CREATED);
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<UUID> updateCountry(
            @PathVariable(name = "countryId") final UUID countryId,
            @RequestBody @Valid final CountryDTO countryDTO) {
        countryService.update(countryId, countryDTO);
        return ResponseEntity.ok(countryId);
    }

    @DeleteMapping("/{countryId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCountry(
            @PathVariable(name = "countryId") final UUID countryId) {
        final ReferencedWarning referencedWarning = countryService.getReferencedWarning(countryId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        countryService.delete(countryId);
        return ResponseEntity.noContent().build();
    }

}
