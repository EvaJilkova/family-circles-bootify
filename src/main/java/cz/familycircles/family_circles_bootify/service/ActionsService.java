package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Action;
import cz.familycircles.family_circles_bootify.domain.Actions;
import cz.familycircles.family_circles_bootify.domain.Location;
import cz.familycircles.family_circles_bootify.domain.User;
import cz.familycircles.family_circles_bootify.model.ActionsDTO;
import cz.familycircles.family_circles_bootify.repos.ActionRepository;
import cz.familycircles.family_circles_bootify.repos.ActionsRepository;
import cz.familycircles.family_circles_bootify.repos.LocationRepository;
import cz.familycircles.family_circles_bootify.repos.UserRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ActionsService {

    private final ActionsRepository actionsRepository;
    private final ActionRepository actionRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public ActionsService(final ActionsRepository actionsRepository,
            final ActionRepository actionRepository, final LocationRepository locationRepository,
            final UserRepository userRepository) {
        this.actionsRepository = actionsRepository;
        this.actionRepository = actionRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public List<ActionsDTO> findAll() {
        final List<Actions> actionses = actionsRepository.findAll(Sort.by("actionsId"));
        return actionses.stream()
                .map(actions -> mapToDTO(actions, new ActionsDTO()))
                .toList();
    }

    public ActionsDTO get(final UUID actionsId) {
        return actionsRepository.findById(actionsId)
                .map(actions -> mapToDTO(actions, new ActionsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ActionsDTO actionsDTO) {
        final Actions actions = new Actions();
        mapToEntity(actionsDTO, actions);
        return actionsRepository.save(actions).getActionsId();
    }

    public void update(final UUID actionsId, final ActionsDTO actionsDTO) {
        final Actions actions = actionsRepository.findById(actionsId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(actionsDTO, actions);
        actionsRepository.save(actions);
    }

    public void delete(final UUID actionsId) {
        actionsRepository.deleteById(actionsId);
    }

    private ActionsDTO mapToDTO(final Actions actions, final ActionsDTO actionsDTO) {
        actionsDTO.setActionsId(actions.getActionsId());
        actionsDTO.setActionId(actions.getActionId() == null ? null : actions.getActionId().getActionId());
        actionsDTO.setLocationId(actions.getLocationId() == null ? null : actions.getLocationId().getLocationId());
        actionsDTO.setUserId(actions.getUserId() == null ? null : actions.getUserId().getUserId());
        return actionsDTO;
    }

    private Actions mapToEntity(final ActionsDTO actionsDTO, final Actions actions) {
        final Action actionId = actionsDTO.getActionId() == null ? null : actionRepository.findById(actionsDTO.getActionId())
                .orElseThrow(() -> new NotFoundException("actionId not found"));
        actions.setActionId(actionId);
        final Location locationId = actionsDTO.getLocationId() == null ? null : locationRepository.findById(actionsDTO.getLocationId())
                .orElseThrow(() -> new NotFoundException("locationId not found"));
        actions.setLocationId(locationId);
        final User userId = actionsDTO.getUserId() == null ? null : userRepository.findById(actionsDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        actions.setUserId(userId);
        return actions;
    }

}
