package project.data;

import project.data.entity.SQLConnection;
import project.logic.DTO.NotificationDTO;
import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SQLNotificationsDAO implements NotificationsDAO
{
    private Connection connection;

    public SQLNotificationsDAO()
    {
        connection = SQLConnection.getInstance().getConnection();
    }

    public List<NotificationDTO> getUserNotifications(UserDTO user)
    {
        List<NotificationDTO> result = new LinkedList<>();
        try(PreparedStatement statement = connection.prepareStatement("select TaskID , Sender , Reciever , Message from Notifications where Reciever = ?"))
        {
            statement.setString(1 , user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                result.add(new NotificationDTO(resultSet.getInt(1) ,
                        resultSet.getString(2) ,
                        resultSet.getString(3) ,
                        resultSet.getString(4)));
            }
            return result;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void add(NotificationDTO notification)
    {
        try(PreparedStatement statement = connection.prepareStatement("insert into Notifications values(? , ? , ? , ?)"))
        {
            basicNotificationFill(notification , statement);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(NotificationDTO notification)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Notifications where TaskID = ? and Sender = ? and Reciever = ? and Message = ?"))
        {
            basicNotificationFill(notification , statement);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(TaskDTO task)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Notifications where TaskID = ?"))
        {
            statement.setInt(1 , task.getId());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(UserDTO user)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Notifications where Reciever = ?"))
        {
            statement.setString(1 , user.getLogin());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(TaskDTO task , UserDTO user)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Notifications where TaskID = ? and Reciever = ?"))
        {
            statement.setInt(1 , task.getId());
            statement.setString(2 , user.getLogin());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void basicNotificationFill(NotificationDTO notification , PreparedStatement statement) throws SQLException
    {
        statement.setInt(1 , notification.getTaskID());
        statement.setString(2 , notification.getSender());
        statement.setString(3 , notification.getReciever());
        statement.setString(4 , notification.getMessage());
        statement.execute();
    }
}
