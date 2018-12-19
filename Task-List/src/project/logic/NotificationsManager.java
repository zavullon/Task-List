package project.logic;

import project.logic.DTO.NotificationDTO;
import project.logic.DTO.UserDTO;

import java.util.List;

public interface NotificationsManager
{
    List<NotificationDTO> getUserNotification(UserDTO user);

    void addNotification(NotificationDTO notification);

    void deleteNotification(NotificationDTO notification);
}
