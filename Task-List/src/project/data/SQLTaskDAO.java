package project.data;

import project.data.entity.SQLConnection;
import project.logic.DTO.TaskDTO;
import project.util.Date;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SQLTaskDAO implements TaskDAO
{
    private Connection connection;

    public SQLTaskDAO()
    {
        connection = SQLConnection.getInstance().getConnection();
    }

    public List<TaskDTO> getAll()
    {
        List<TaskDTO> result = new LinkedList<>();
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "select TaskID , " +
                "Date , " +
                "Note , " +
                "Type , " +
                "Access , " +
                "Login " +
                "from Tasks"))
        {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(new TaskDTO(resultSet.getInt(1) ,
                    new project.util.Date(resultSet.getString(2)) ,
                    resultSet.getString(3) ,
                    resultSet.getString(4) ,
                    resultSet.getString(5) ,
                    resultSet.getString(6)));
            return result;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public TaskDTO get(TaskDTO task)
    {
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "select TaskID , " +
                "Date , " +
                "Note , " +
                "Type , " +
                "Access , " +
                "Login " +
                "from Tasks " +
                "where Date = ? " +
                "and Note = ? " +
                "and Type = ? " +
                "and Access = ? " +
                "and Login = ?"))
        {
            statement.setString(1 , task.getDate().toString());
            statement.setString(2 , task.getNote());
            statement.setString(3 , task.getType());
            statement.setString(4 , task.getAccess());
            statement.setString(5 , task.getCreatorLogin());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new TaskDTO(resultSet.getInt(1) ,
                    new project.util.Date(resultSet.getString(2)) ,
                    resultSet.getString(3) ,
                    resultSet.getString(4) ,
                    resultSet.getString(5) ,
                    resultSet.getString(6));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public TaskDTO getByID(int id)
    {
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "select TaskID , " +
                "Date , " +
                "Note , " +
                "Type , " +
                "Access , " +
                "Login " +
                "from Tasks " +
                "where TaskID = ?"))
        {
            statement.setInt(1 , id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new TaskDTO(resultSet.getInt(1) ,
                    new Date(resultSet.getString(2)) ,
                    resultSet.getString(3) ,
                    resultSet.getString(4) ,
                    resultSet.getString(5) ,
                    resultSet.getString(6));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(TaskDTO task)
    {
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "delete from Tasks where TaskID = ?"))
        {
            statement.setInt(1 , this.get(task).getId());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void add(TaskDTO task)
    {
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "insert into Tasks values(? , ? , ? , ? , ? , ?)"))
        {
            statement.setInt(1 , getMaxID() + 1);
            statement.setString(2 , task.getDate().toString());
            statement.setString(3 , task.getNote());
            statement.setString(4 , task.getType());
            statement.setString(5 , task.getAccess());
            statement.setString(6 , task.getCreatorLogin());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void update(TaskDTO olTaskDTO , TaskDTO newTask)
    {
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "update Tasks " +
                "set Date = ? ," +
                " Note = ? ," +
                " Type = ? ," +
                " Access = ?" +
                " where Date = ?" +
                " and Note = ?" +
                " and Type = ?" +
                " and Access = ?"))
        {
            statement.setString(5 , olTaskDTO.getDate().toString());
            statement.setString(6 , olTaskDTO.getNote());
            statement.setString(7 , olTaskDTO.getType());
            statement.setString(8 , olTaskDTO.getAccess());
            statement.setString(1 , newTask.getDate().toString());
            statement.setString(2 , newTask.getNote());
            statement.setString(3 , newTask.getType());
            statement.setString(4 , newTask.getAccess());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getMaxID()
    {
        try(PreparedStatement statement = connection.prepareStatement("set dateformat ymd " +
                "select max(TaskID) , count(TaskID) from Tasks"))
        {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            if(resultSet.getInt(2) > 0) return resultSet.getInt(1);
            else return -1;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
