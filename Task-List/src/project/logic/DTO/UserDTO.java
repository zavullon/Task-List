package project.logic.DTO;

public class UserDTO
{
    private String login;
    private String password;
    private String accessType;

    public UserDTO(String login , String password , String accessType)
    {
        this.login = login;
        this.password = password;
        this.accessType = accessType;
    }

    public UserDTO(String login , String password)
    {
        this.login = login;
        this.password = password;
        this.accessType = null;
    }

    public String getLogin()
    {
        return this.login;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getAccessType()
    {
        return this.accessType;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof UserDTO)
        {
            return ((UserDTO) obj).getLogin().equals(this.getLogin());
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public String toString()
    {
        return this.login;
    }
}
