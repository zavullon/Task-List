package project.logic.entities;

import project.logic.DTO.TaskDTO;
import project.logic.entities.BLUser;
import project.util.Date;

import java.util.ArrayList;

public class BLTask implements Comparable<BLTask>
{
    private int id;
    private Date date;
    private String note;
    private String type;
    private String access;
    private String creatorLogin;

    public BLTask(int id , Date date , String note , String type , String access , String creatorLogin)
    {
        this.id = id;
        this.date = new Date(date);
        this.note = note;
        this.type = type;
        this.access = access;
        this.creatorLogin = creatorLogin;
    }

    public BLTask(TaskDTO task)
    {
        this.id = task.getId();
        this.date = task.getDate();
        this.note = task.getNote();
        this.type = task.getType();
        this.access = task.getAccess();
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

    public String toString()
    {
        return this.getId() + " " + this.date.toString() + " " + this.note + " " + this.getType() + " " + this.getAccess();
    }

    @Override
    public int compareTo(BLTask o)
    {
        return this.date.compareTo(o.getDate());
    }

    public boolean equals(BLTask o)
    {
        return this.date.equals(o.getDate()) && this.note == o.getNote() && this.type.equals(o.getType());
    }

    public boolean before(BLTask o)
    {
        return this.compareTo(o) <= 0;
    }

    public boolean after(BLTask o)
    {
        return this.compareTo(o) >= 0;
    }
}
