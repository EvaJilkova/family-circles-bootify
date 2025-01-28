package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.model.CategoryDTO;
import cz.familycircles.family_circles_bootify.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("category") final CategoryDTO categoryDTO) {
        return "category/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("category") @Valid final CategoryDTO categoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "category/add";
        }
        categoryService.create(categoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("category.create.success"));
        return "redirect:/categories";
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(@PathVariable(name = "categoryId") final UUID categoryId,
            final Model model) {
        model.addAttribute("category", categoryService.get(categoryId));
        return "category/edit";
    }

    @PostMapping("/edit/{categoryId}")
    public String edit(@PathVariable(name = "categoryId") final UUID categoryId,
            @ModelAttribute("category") @Valid final CategoryDTO categoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "category/edit";
        }
        categoryService.update(categoryId, categoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("category.update.success"));
        return "redirect:/categories";
    }

    @PostMapping("/delete/{categoryId}")
    public String delete(@PathVariable(name = "categoryId") final UUID categoryId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = categoryService.getReferencedWarning(categoryId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            categoryService.delete(categoryId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("category.delete.success"));
        }
        return "redirect:/categories";
    }

}
