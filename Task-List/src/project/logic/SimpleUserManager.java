package project.logic;

import project.data.NotificationsDAO;
import project.data.RelationsDAO;
import project.data.TaskDAO;
import project.data.UserDAO;
import project.logic.DTO.NotificationDTO;
import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;

import java.util.LinkedList;
import java.util.List;

public class SimpleUserManager implements UserManager
{
    private final NotificationsDAO notificationsDAO;
    private final RelationsDAO relationsDAO;
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    public SimpleUserManager(NotificationsDAO notificationsDAO , RelationsDAO relationsDAO , TaskDAO taskDAO , UserDAO userDAO)
    {
        this.notificationsDAO = notificationsDAO;
        this.relationsDAO = relationsDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void deleteUserFromTask(TaskDTO task , UserDTO user)
    {
        relationsDAO.delete(task , user);
        notificationsDAO.delete(task , user);
        notificationsDAO.add(new NotificationDTO(task.getId() , task.getCreatorLogin() , user.getLogin() , "Removed"));
    }

    @Override
    public boolean addUser(UserDTO user)
    {
        return userDAO.add(user);
    }

    @Override
    public UserDTO getUser(UserDTO user)
    {
        return userDAO.get(user);
    }

    @Override
    public List<UserDTO> getUsersByTask(TaskDTO task)
    {
        List<UserDTO> result = new LinkedList<>();
        relationsDAO.getUserLoginsByTask(task).forEach((l) -> result.add(userDAO.getByLogin(l)));
        return result;
    }

    @Override
    public List<UserDTO> getAllUsers()
    {
        return userDAO.getAll();
    }

    @Override
    public List<UserDTO> getPotentialUsers(TaskDTO task)
    {
        List<UserDTO> taskUsers = new LinkedList<>();
        relationsDAO.getUserLoginsByTask(task).forEach((l) -> taskUsers.add(userDAO.getByLogin(l)));
        List<UserDTO> allUsers = new LinkedList<>();
        allUsers = userDAO.getAll();
        allUsers.removeIf((u) -> taskUsers.contains(u));
        return allUsers;
    }

    @Override
    public UserDTO getUserByLogin(String login)
    {
        return userDAO.getByLogin(login);
    }

    @Override
    public void addUserToTask(TaskDTO task , UserDTO user)
    {
        TaskDTO taskWithID = taskDAO.get(task);
        if(!relationsDAO.getTaskIDsByUser(user).contains(taskWithID.getId()))
        {
            relationsDAO.add(taskDAO.get(task) , user);
        }
    }
}
