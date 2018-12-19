package project.logic;

import project.data.NotificationsDAO;
import project.data.RelationsDAO;
import project.data.TaskDAO;
import project.data.UserDAO;
import project.logic.DTO.NotificationDTO;
import project.logic.DTO.UserDTO;

import java.util.List;

public class SimpleNotificationsManager implements NotificationsManager
{
    private final NotificationsDAO notificationsDAO;
    private final RelationsDAO relationsDAO;
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    public SimpleNotificationsManager(NotificationsDAO notificationsDAO , RelationsDAO relationsDAO , TaskDAO taskDAO , UserDAO userDAO)
    {
        this.notificationsDAO = notificationsDAO;
        this.relationsDAO = relationsDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<NotificationDTO> getUserNotification(UserDTO user)
    {
        return notificationsDAO.getUserNotifications(user);
    }

    @Override
    public void addNotification(NotificationDTO notification)
    {
        notificationsDAO.add(notification);
    }

    @Override
    public void deleteNotification(NotificationDTO notification)
    {
        notificationsDAO.delete(notification);
    }
}
