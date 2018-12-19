package project.userinterface.entities;

import project.logic.DTO.TaskDTO;
import project.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class UITask implements Comparable<UITask>
{
    private int id;
    private Date date;
    private String note;
    private String type;
    private String access;
    private String creatorLogin;

    public UITask(int id , Date date , String note , String type , String access , String creatorLogin)
    {
        this.id = id;
        this.date = new Date(date);
        this.note = note;
        this.type = type;
        this.access = access;
        this.creatorLogin = creatorLogin;
    }

    public UITask(TaskDTO task)
    {
        this.id = task.getId();
        this.date = task.getDate();
        this.note = task.getNote();
        this.type = task.getType();
        this.access = task.getAccess();
        this.creatorLogin = task.getCreatorLogin();
    }

    public UITask(UIUser user)
    {
        this.date = new Date(LocalDateTime.now().getYear() ,
                 LocalDateTime.now().getMonth().getValue() ,
                       LocalDateTime.now().getDayOfMonth() ,
                             LocalDateTime.now().getHour() ,
                             LocalDateTime.now().getSecond());
        this.note = "";
        this.type = null;
        this.access = null;
        this.creatorLogin = user.getLogin();
    }

    public UITask(UITask task)
    {
        this.date = task.getDate();
        this.note = task.getNote();
        this.access = task.getAccess();
        this.type = task.getType();
        this.creatorLogin = task.getCreatorLogin();
    }

    public TaskDTO toDTO()
    {
        return new TaskDTO(this.id , this.date , this.note , this.type , this.access , this.creatorLogin);
    }

    public int getId()
    {
        return this.id;
    }

    public Date getDate()
    {
        return this.date;
    }

    public String getNote()
    {
        return this.note;
    }

    public String getType()
    {
        return this.type;
    }

    public String getAccess()
    {
        return this.access;
    }

    public String getCreatorLogin()
    {
        return this.creatorLogin;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setAccess(String access)
    {
        this.access = access;
    }

    @Override
    public String toString()
    {
        return this.getId() + " " + this.date.toString() + " " + this.note + " " + this.getType() + " " + this.getAccess();
    }

    @Override
    public int compareTo(UITask o)
    {
        return this.date.compareTo(o.getDate());
    }

    public boolean equals(UITask o)
    {
        return this.date.equals(o.getDate()) && this.note.equals(o.getNote()) && this.type.equals(o.getType()) &&
                this.access.equals(o.getAccess()) && this.creatorLogin.equals(o.getCreatorLogin());
    }

    public boolean before(UITask o)
    {
        return this.compareTo(o) <= 0;
    }

    public boolean after(UITask o)
    {
        return this.compareTo(o) >= 0;
    }
}
