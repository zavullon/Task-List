package project.logic;

import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;

import java.util.List;

public interface UserManager
{
    void addUserToTask(TaskDTO task , UserDTO user);

    void deleteUserFromTask(TaskDTO task , UserDTO user);

    boolean addUser(UserDTO user);

    UserDTO getUser(UserDTO user);

    List<UserDTO> getUsersByTask(TaskDTO task);

    List<UserDTO> getAllUsers();

    List<UserDTO> getPotentialUsers(TaskDTO task);

    UserDTO getUserByLogin(String login);
}
