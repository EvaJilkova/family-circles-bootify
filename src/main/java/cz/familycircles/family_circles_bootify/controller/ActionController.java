package cz.familycircles.family_circles_bootify.controller;

import cz.familycircles.family_circles_bootify.model.ActionDTO;
import cz.familycircles.family_circles_bootify.model.ActionTypeEnum;
import cz.familycircles.family_circles_bootify.service.ActionService;
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
@RequestMapping("/actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(final ActionService actionService) {
        this.actionService = actionService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("actionTypeValues", ActionTypeEnum.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("actions", actionService.findAll());
        return "action/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("action") final ActionDTO actionDTO) {
        return "action/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("action") @Valid final ActionDTO actionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "action/add";
        }
        actionService.create(actionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("action.create.success"));
        return "redirect:/actions";
    }

    @GetMapping("/edit/{actionId}")
    public String edit(@PathVariable(name = "actionId") final UUID actionId, final Model model) {
        model.addAttribute("action", actionService.get(actionId));
        return "action/edit";
    }

    @PostMapping("/edit/{actionId}")
    public String edit(@PathVariable(name = "actionId") final UUID actionId,
            @ModelAttribute("action") @Valid final ActionDTO actionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "action/edit";
        }
        actionService.update(actionId, actionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("action.update.success"));
        return "redirect:/actions";
    }

    @PostMapping("/delete/{actionId}")
    public String delete(@PathVariable(name = "actionId") final UUID actionId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = actionService.getReferencedWarning(actionId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            actionService.delete(actionId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("action.delete.success"));
        }
        return "redirect:/actions";
    }

}
