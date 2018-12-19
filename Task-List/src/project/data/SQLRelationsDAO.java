package project.data;

import project.data.entity.SQLConnection;
import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;
import project.util.Request;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SQLRelationsDAO implements RelationsDAO
{
    private Connection connection;

    public SQLRelationsDAO()
    {
        connection = SQLConnection.getInstance().getConnection();
    }

    public List<Integer> getTaskIDsByUser(UserDTO user , Request request)
    {
        try(PreparedStatement statement = connection.prepareStatement("select TaskID from Relations where Login = ?"))
        {
            List<Integer> result = new LinkedList<>();
            statement.setString(1 , user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                result.add(resultSet.getInt(1));
                request.addUpdate();
            }
            request.doneUpdate();
            return result;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> getTaskIDsByUser(UserDTO user)
    {
        List<Integer> result = new LinkedList<>();
        try(PreparedStatement statement = connection.prepareStatement("select TaskID from Relations where Login = ?"))
        {
            statement.setString(1 , user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                result.add(resultSet.getInt(1));
            }
            return result;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getUserLoginsByTask(TaskDTO task)
    {
        List<String> result = new LinkedList<>();
        try(PreparedStatement statement = connection.prepareStatement("select Login from Relations where TaskID = ?"))
        {
            statement.setInt(1 , task.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                result.add(resultSet.getString(1));
            }
            return result;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void add(TaskDTO task , UserDTO user)
    {
        try(PreparedStatement statement = connection.prepareStatement("insert into Relations values(? , ?)"))
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

    public void delete(TaskDTO task , UserDTO user)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Relations where TaskID = ? and Login = ?"))
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

    public void delete(TaskDTO task)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Relations where TaskID = ?"))
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
        try(PreparedStatement statement = connection.prepareStatement("delete from Relations where Login = ?"))
        {
            statement.setString(1 , user.getLogin());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
