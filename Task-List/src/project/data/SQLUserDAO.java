package project.data;

import project.data.entity.SQLConnection;
import project.logic.DTO.UserDTO;
import project.util.PasswordCoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDAO implements UserDAO
{
    private Connection connection;

    public SQLUserDAO()
    {
        connection = SQLConnection.getInstance().getConnection();
    }

    public UserDTO get(UserDTO user)
    {
        try(PreparedStatement statement = connection.prepareStatement("select Login , " +
                "Password , " +
                "Access " +
                "from Users " +
                "where Login = ? " +
                "and Password = ?"))
        {
            statement.setString(1 , user.getLogin());
            statement.setString(2 , PasswordCoder.code(user.getPassword()));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                return new UserDTO(resultSet.getString(1) , resultSet.getString(2) , resultSet.getString(3));
            }
            else
            {
                return null;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public UserDTO getByLogin(String login)
    {
        try(PreparedStatement statement = connection.prepareStatement("select Login , " +
                "Password , " +
                "Access " +
                "from Users " +
                "where Login = ?"))
        {
            statement.setString(1 , login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new UserDTO(resultSet.getString(1) , resultSet.getString(2) , resultSet.getString(3));
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserDTO> getAll()
    {
        ArrayList<UserDTO> result = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select Login , Password , Access from Users"))
        {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(new UserDTO(resultSet.getString(1) ,
                    resultSet.getString(2) ,
                    resultSet.getString(3)));
            return result;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public boolean add(UserDTO user)
    {
        try(PreparedStatement verify = connection.prepareStatement("select Login from Users where Login = ?"))
        {
            verify.setString(1 , user.getLogin());
            ResultSet verifyResult = verify.executeQuery();
            if(verifyResult.next()) return false;
            else
            {
                PreparedStatement statement = connection.prepareStatement("insert into Users values(? , ? , ?)");
                statement.setString(1 , user.getLogin());
                statement.setString(2 , PasswordCoder.code(user.getPassword()));
                statement.setString(3 , user.getAccessType());
                statement.execute();
                return true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(UserDTO user)
    {
        try(PreparedStatement statement = connection.prepareStatement("delete from Users where Login = ?"))
        {
            statement.setString(1 , user.getLogin());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void update(UserDTO oldUser , UserDTO newUser)
    {
        try(PreparedStatement statement = connection.prepareStatement("update Users " +
                "set Password = ? ," +
                " Access = ?" +
                " where Login = ?"))
        {
            statement.setString(1 , PasswordCoder.code(newUser.getPassword()));
            statement.setString(2 , newUser.getAccessType());
            statement.setString(3 , oldUser.getLogin());
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
