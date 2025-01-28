package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.domain.Region;
import cz.familycircles.family_circles_bootify.model.CountryDTO;
import cz.familycircles.family_circles_bootify.repos.RegionRepository;
import cz.familycircles.family_circles_bootify.service.CountryService;
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
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;
    private final RegionRepository regionRepository;

    public CountryController(final CountryService countryService,
            final RegionRepository regionRepository) {
        this.countryService = countryService;
        this.regionRepository = regionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("regionIdValues", regionRepository.findAll(Sort.by("regionId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Region::getRegionId, Region::getRegionCode)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "country/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("country") final CountryDTO countryDTO) {
        return "country/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("country") @Valid final CountryDTO countryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "country/add";
        }
        countryService.create(countryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("country.create.success"));
        return "redirect:/countries";
    }

    @GetMapping("/edit/{countryId}")
    public String edit(@PathVariable(name = "countryId") final UUID countryId, final Model model) {
        model.addAttribute("country", countryService.get(countryId));
        return "country/edit";
    }

    @PostMapping("/edit/{countryId}")
    public String edit(@PathVariable(name = "countryId") final UUID countryId,
            @ModelAttribute("country") @Valid final CountryDTO countryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "country/edit";
        }
        countryService.update(countryId, countryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("country.update.success"));
        return "redirect:/countries";
    }

    @PostMapping("/delete/{countryId}")
    public String delete(@PathVariable(name = "countryId") final UUID countryId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = countryService.getReferencedWarning(countryId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            countryService.delete(countryId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("country.delete.success"));
        }
        return "redirect:/countries";
    }

}
