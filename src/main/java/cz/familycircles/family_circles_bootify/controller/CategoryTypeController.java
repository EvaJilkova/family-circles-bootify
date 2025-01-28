package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.model.CategoryTypeDTO;
import cz.familycircles.family_circles_bootify.service.CategoryTypeService;
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
@RequestMapping("/categoryTypes")
public class CategoryTypeController {

    private final CategoryTypeService categoryTypeService;

    public CategoryTypeController(final CategoryTypeService categoryTypeService) {
        this.categoryTypeService = categoryTypeService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("categoryTypes", categoryTypeService.findAll());
        return "categoryType/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("categoryType") final CategoryTypeDTO categoryTypeDTO) {
        return "categoryType/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("categoryType") @Valid final CategoryTypeDTO categoryTypeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "categoryType/add";
        }
        categoryTypeService.create(categoryTypeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("categoryType.create.success"));
        return "redirect:/categoryTypes";
    }

    @GetMapping("/edit/{categoryTypeId}")
    public String edit(@PathVariable(name = "categoryTypeId") final UUID categoryTypeId,
            final Model model) {
        model.addAttribute("categoryType", categoryTypeService.get(categoryTypeId));
        return "categoryType/edit";
    }

    @PostMapping("/edit/{categoryTypeId}")
    public String edit(@PathVariable(name = "categoryTypeId") final UUID categoryTypeId,
            @ModelAttribute("categoryType") @Valid final CategoryTypeDTO categoryTypeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "categoryType/edit";
        }
        categoryTypeService.update(categoryTypeId, categoryTypeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("categoryType.update.success"));
        return "redirect:/categoryTypes";
    }

    @PostMapping("/delete/{categoryTypeId}")
    public String delete(@PathVariable(name = "categoryTypeId") final UUID categoryTypeId,
            final RedirectAttributes redirectAttributes) {
        categoryTypeService.delete(categoryTypeId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("categoryType.delete.success"));
        return "redirect:/categoryTypes";
    }

}
