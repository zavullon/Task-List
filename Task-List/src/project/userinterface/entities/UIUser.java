package project.userinterface.entities;

import project.logic.DTO.UserDTO;

public class UIUser
{
    private String login;
    private String password;
    private String accessType;
    private Boolean isChosen = false;

    public UIUser(String login , String password , String accessType)
    {
        this.login = login;
        this.password = password;
        this.accessType = accessType;
    }

    public UIUser(String login , String password)
    {
        this.login = login;
        this.password = password;
        this.accessType = null;
    }

    public UIUser(UserDTO user)
    {
        if(user != null)
        {
            this.login = user.getLogin();
            this.password = user.getPassword();
            this.accessType = user.getAccessType();
        } else
        {
            this.login = this.password = this.accessType = null;
        }
    }

    public UserDTO toDTO()
    {
        return new UserDTO(this.getLogin() , this.getPassword() , this.getAccessType());
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

    public Boolean getChosen()
    {
        return this.isChosen;
    }

    public void setChosen(Boolean chosen)
    {
        this.isChosen = chosen;
    }

    public boolean isNull()
    {
        return this.login == null && this.password == null && this.accessType == null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof UIUser)
        {
            return ((UIUser) obj).getLogin().equals(this.getLogin());
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
