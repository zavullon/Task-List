package project.data;

import project.logic.DTO.NotificationDTO;
import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;

import java.util.List;

public interface NotificationsDAO
{
    public List<NotificationDTO> getUserNotifications(UserDTO user);

    public void add(NotificationDTO notification);

    public void delete(NotificationDTO notification);

    public void delete(TaskDTO task);

    public void delete(UserDTO user);

    public void delete(TaskDTO task , UserDTO user);
}
