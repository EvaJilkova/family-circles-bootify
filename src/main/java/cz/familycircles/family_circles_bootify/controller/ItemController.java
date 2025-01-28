package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.domain.Category;
import cz.familycircles.family_circles_bootify.model.ItemDTO;
import cz.familycircles.family_circles_bootify.repos.CategoryRepository;
import cz.familycircles.family_circles_bootify.service.ItemService;
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
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    public ItemController(final ItemService itemService,
            final CategoryRepository categoryRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("categoryIdValues", categoryRepository.findAll(Sort.by("categoryId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Category::getCategoryId, Category::getCategoryName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("items", itemService.findAll());
        return "item/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("item") final ItemDTO itemDTO) {
        return "item/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("item") @Valid final ItemDTO itemDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "item/add";
        }
        itemService.create(itemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item.create.success"));
        return "redirect:/items";
    }

    @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable(name = "itemId") final UUID itemId, final Model model) {
        model.addAttribute("item", itemService.get(itemId));
        return "item/edit";
    }

    @PostMapping("/edit/{itemId}")
    public String edit(@PathVariable(name = "itemId") final UUID itemId,
            @ModelAttribute("item") @Valid final ItemDTO itemDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "item/edit";
        }
        itemService.update(itemId, itemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("item.update.success"));
        return "redirect:/items";
    }

    @PostMapping("/delete/{itemId}")
    public String delete(@PathVariable(name = "itemId") final UUID itemId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = itemService.getReferencedWarning(itemId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            itemService.delete(itemId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("item.delete.success"));
        }
        return "redirect:/items";
    }

}
