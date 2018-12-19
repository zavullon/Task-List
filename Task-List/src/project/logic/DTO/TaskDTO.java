package project.logic.DTO;

import project.util.Date;

public class TaskDTO implements Comparable<TaskDTO>
{
    private int id;
    private Date date;
    private String note;
    private String type;
    private String access;
    private String creatorLogin;

    public TaskDTO(int id , Date date , String note , String type , String access , String creatorLogin)
    {
        this.id = id;
        this.date = new Date(date);
        this.note = note;
        this.type = type;
        this.access = access;
        this.creatorLogin = creatorLogin;
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

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return this.getId() + " " + this.date.toString() + " " + this.note + " " + this.getType() + " " + this.getAccess();
    }

    @Override
    public int compareTo(TaskDTO o)
    {
        return this.date.compareTo(o.getDate());
    }

    //TODO
    public boolean equals(TaskDTO o)
    {
        return this.date.equals(o.getDate()) && this.note == o.getNote() && this.type.equals(o.getType());
    }

    public boolean before(TaskDTO o)
    {
        return this.compareTo(o) <= 0;
    }

    public boolean after(TaskDTO o)
    {
        return this.compareTo(o) >= 0;
    }
}
