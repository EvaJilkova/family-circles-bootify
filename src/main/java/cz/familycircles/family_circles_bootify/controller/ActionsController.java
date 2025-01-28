package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.domain.Action;
import cz.familycircles.family_circles_bootify.domain.Location;
import cz.familycircles.family_circles_bootify.domain.User;
import cz.familycircles.family_circles_bootify.model.ActionsDTO;
import cz.familycircles.family_circles_bootify.repos.ActionRepository;
import cz.familycircles.family_circles_bootify.repos.LocationRepository;
import cz.familycircles.family_circles_bootify.repos.UserRepository;
import cz.familycircles.family_circles_bootify.service.ActionsService;
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
@RequestMapping("/actionss")
public class ActionsController {

    private final ActionsService actionsService;
    private final ActionRepository actionRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public ActionsController(final ActionsService actionsService,
            final ActionRepository actionRepository, final LocationRepository locationRepository,
            final UserRepository userRepository) {
        this.actionsService = actionsService;
        this.actionRepository = actionRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("actionIdValues", actionRepository.findAll(Sort.by("actionId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Action::getActionId, Action::getName)));
        model.addAttribute("locationIdValues", locationRepository.findAll(Sort.by("locationId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Location::getLocationId, Location::getLocationId)));
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUserName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("actionses", actionsService.findAll());
        return "actions/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("actions") final ActionsDTO actionsDTO) {
        return "actions/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("actions") @Valid final ActionsDTO actionsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "actions/add";
        }
        actionsService.create(actionsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("actions.create.success"));
        return "redirect:/actionss";
    }

    @GetMapping("/edit/{actionsId}")
    public String edit(@PathVariable(name = "actionsId") final UUID actionsId, final Model model) {
        model.addAttribute("actions", actionsService.get(actionsId));
        return "actions/edit";
    }

    @PostMapping("/edit/{actionsId}")
    public String edit(@PathVariable(name = "actionsId") final UUID actionsId,
            @ModelAttribute("actions") @Valid final ActionsDTO actionsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "actions/edit";
        }
        actionsService.update(actionsId, actionsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("actions.update.success"));
        return "redirect:/actionss";
    }

    @PostMapping("/delete/{actionsId}")
    public String delete(@PathVariable(name = "actionsId") final UUID actionsId,
            final RedirectAttributes redirectAttributes) {
        actionsService.delete(actionsId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("actions.delete.success"));
        return "redirect:/actionss";
    }

}
