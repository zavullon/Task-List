package project.data.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection
{
    private String url = "jdbc:sqlserver://169.254.220.86:63293;database=ToDoListDB";
    private String login = "admin";
    private String password = "QEADZCwsx";

    private Connection connection;

    private static SQLConnection ourInstance = new SQLConnection();

    public static SQLConnection getInstance()
    {
        return ourInstance;
    }

    private SQLConnection()
    {
        try
        {
            connection = DriverManager.getConnection(url , login , password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}
