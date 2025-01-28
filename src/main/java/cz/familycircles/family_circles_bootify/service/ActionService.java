package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Action;
import cz.familycircles.family_circles_bootify.domain.Actions;
import cz.familycircles.family_circles_bootify.model.ActionDTO;
import cz.familycircles.family_circles_bootify.repos.ActionRepository;
import cz.familycircles.family_circles_bootify.repos.ActionsRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final ActionsRepository actionsRepository;

    public ActionService(final ActionRepository actionRepository,
            final ActionsRepository actionsRepository) {
        this.actionRepository = actionRepository;
        this.actionsRepository = actionsRepository;
    }

    public List<ActionDTO> findAll() {
        final List<Action> actions = actionRepository.findAll(Sort.by("actionId"));
        return actions.stream()
                .map(action -> mapToDTO(action, new ActionDTO()))
                .toList();
    }

    public ActionDTO get(final UUID actionId) {
        return actionRepository.findById(actionId)
                .map(action -> mapToDTO(action, new ActionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ActionDTO actionDTO) {
        final Action action = new Action();
        mapToEntity(actionDTO, action);
        return actionRepository.save(action).getActionId();
    }

    public void update(final UUID actionId, final ActionDTO actionDTO) {
        final Action action = actionRepository.findById(actionId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(actionDTO, action);
        actionRepository.save(action);
    }

    public void delete(final UUID actionId) {
        actionRepository.deleteById(actionId);
    }

    private ActionDTO mapToDTO(final Action action, final ActionDTO actionDTO) {
        actionDTO.setActionId(action.getActionId());
        actionDTO.setName(action.getName());
        actionDTO.setDescription(action.getDescription());
        actionDTO.setActionType(action.getActionType());
        return actionDTO;
    }

    private Action mapToEntity(final ActionDTO actionDTO, final Action action) {
        action.setName(actionDTO.getName());
        action.setDescription(actionDTO.getDescription());
        action.setActionType(actionDTO.getActionType());
        return action;
    }

    public boolean nameExists(final String name) {
        return actionRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID actionId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Action action = actionRepository.findById(actionId)
                .orElseThrow(NotFoundException::new);
        final Actions actionIdActions = actionsRepository.findFirstByActionId(action);
        if (actionIdActions != null) {
            referencedWarning.setKey("action.actions.actionId.referenced");
            referencedWarning.addParam(actionIdActions.getActionsId());
            return referencedWarning;
        }
        return null;
    }

}
