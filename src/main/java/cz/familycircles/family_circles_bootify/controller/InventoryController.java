package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.domain.Item;
import cz.familycircles.family_circles_bootify.domain.User;
import cz.familycircles.family_circles_bootify.model.InventoryDTO;
import cz.familycircles.family_circles_bootify.repos.ItemRepository;
import cz.familycircles.family_circles_bootify.repos.UserRepository;
import cz.familycircles.family_circles_bootify.service.InventoryService;
import cz.familycircles.family_circles_bootify.util.CustomCollectors;
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
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public InventoryController(final InventoryService inventoryService,
            final ItemRepository itemRepository, final UserRepository userRepository) {
        this.inventoryService = inventoryService;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("itemIdValues", itemRepository.findAll(Sort.by("itemId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Item::getItemId, Item::getItemName)));
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUserName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("inventories", inventoryService.findAll());
        return "inventory/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("inventory") final InventoryDTO inventoryDTO) {
        return "inventory/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("inventory") @Valid final InventoryDTO inventoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inventory/add";
        }
        inventoryService.create(inventoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inventory.create.success"));
        return "redirect:/inventories";
    }

    @GetMapping("/edit/{inventoryId}")
    public String edit(@PathVariable(name = "inventoryId") final UUID inventoryId,
            final Model model) {
        model.addAttribute("inventory", inventoryService.get(inventoryId));
        return "inventory/edit";
    }

    @PostMapping("/edit/{inventoryId}")
    public String edit(@PathVariable(name = "inventoryId") final UUID inventoryId,
            @ModelAttribute("inventory") @Valid final InventoryDTO inventoryDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inventory/edit";
        }
        inventoryService.update(inventoryId, inventoryDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inventory.update.success"));
        return "redirect:/inventories";
    }

    @PostMapping("/delete/{inventoryId}")
    public String delete(@PathVariable(name = "inventoryId") final UUID inventoryId,
            final RedirectAttributes redirectAttributes) {
        inventoryService.delete(inventoryId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("inventory.delete.success"));
        return "redirect:/inventories";
    }

}
