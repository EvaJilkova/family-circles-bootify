package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.model.RegionDTO;
import cz.familycircles.family_circles_bootify.service.RegionService;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import cz.familycircles.family_circles_bootify.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
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
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(final RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("regions", regionService.findAll());
        return "region/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("region") final RegionDTO regionDTO) {
        return "region/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("region") @Valid final RegionDTO regionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "region/add";
        }
        regionService.create(regionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("region.create.success"));
        return "redirect:/regions";
    }

    @GetMapping("/edit/{regionId}")
    public String edit(@PathVariable(name = "regionId") final UUID regionId, final Model model) {
        model.addAttribute("region", regionService.get(regionId));
        return "region/edit";
    }

    @PostMapping("/edit/{regionId}")
    public String edit(@PathVariable(name = "regionId") final UUID regionId,
            @ModelAttribute("region") @Valid final RegionDTO regionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "region/edit";
        }
        regionService.update(regionId, regionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("region.update.success"));
        return "redirect:/regions";
    }

    @PostMapping("/delete/{regionId}")
    public String delete(@PathVariable(name = "regionId") final UUID regionId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = regionService.getReferencedWarning(regionId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            regionService.delete(regionId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("region.delete.success"));
        }
        return "redirect:/regions";
    }

}
