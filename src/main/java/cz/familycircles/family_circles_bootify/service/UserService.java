package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Actions;
import cz.familycircles.family_circles_bootify.domain.Inventory;
import cz.familycircles.family_circles_bootify.domain.User;
import cz.familycircles.family_circles_bootify.model.UserDTO;
import cz.familycircles.family_circles_bootify.repos.ActionsRepository;
import cz.familycircles.family_circles_bootify.repos.InventoryRepository;
import cz.familycircles.family_circles_bootify.repos.UserRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActionsRepository actionsRepository;
    private final InventoryRepository inventoryRepository;

    public UserService(final UserRepository userRepository,
            final ActionsRepository actionsRepository,
            final InventoryRepository inventoryRepository) {
        this.userRepository = userRepository;
        this.actionsRepository = actionsRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final UUID userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final UUID userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setLanguage(user.getLanguage());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setLanguage(userDTO.getLanguage());
        return user;
    }

    public boolean userNameExists(final String userName) {
        return userRepository.existsByUserNameIgnoreCase(userName);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final UUID userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final Actions userIdActions = actionsRepository.findFirstByUserId(user);
        if (userIdActions != null) {
            referencedWarning.setKey("user.actions.userId.referenced");
            referencedWarning.addParam(userIdActions.getActionsId());
            return referencedWarning;
        }
        final Inventory userIdInventory = inventoryRepository.findFirstByUserId(user);
        if (userIdInventory != null) {
            referencedWarning.setKey("user.inventory.userId.referenced");
            referencedWarning.addParam(userIdInventory.getInventoryId());
            return referencedWarning;
        }
        return null;
    }

}
