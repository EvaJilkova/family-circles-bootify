package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.domain.Country;
import cz.familycircles.family_circles_bootify.model.LocationDTO;
import cz.familycircles.family_circles_bootify.repos.CountryRepository;
import cz.familycircles.family_circles_bootify.service.LocationService;
import cz.familycircles.family_circles_bootify.util.CustomCollectors;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import cz.familycircles.family_circles_bootify.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final CountryRepository countryRepository;

    public LocationController(final LocationService locationService,
            final CountryRepository countryRepository) {
        this.locationService = locationService;
        this.countryRepository = countryRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("countryIdValues", countryRepository.findAll(Sort.by("countryId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Country::getCountryId, Country::getCountryName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "location/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("location") final LocationDTO locationDTO) {
        return "location/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("location") @Valid final LocationDTO locationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "location/add";
        }
        locationService.create(locationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("location.create.success"));
        return "redirect:/locations";
    }

    @GetMapping("/edit/{locationId}")
    public String edit(@PathVariable(name = "locationId") final UUID locationId,
            final Model model) {
        model.addAttribute("location", locationService.get(locationId));
        return "location/edit";
    }

    @PostMapping("/edit/{locationId}")
    public String edit(@PathVariable(name = "locationId") final UUID locationId,
            @ModelAttribute("location") @Valid final LocationDTO locationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "location/edit";
        }
        locationService.update(locationId, locationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("location.update.success"));
        return "redirect:/locations";
    }

    @PostMapping("/delete/{locationId}")
    public String delete(@PathVariable(name = "locationId") final UUID locationId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = locationService.getReferencedWarning(locationId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            locationService.delete(locationId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("location.delete.success"));
        }
        return "redirect:/locations";
    }

}
