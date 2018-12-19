package project.logic.entities;

import project.logic.DTO.NotificationDTO;

public class BLNotification
{
    private String sender;
    private String reciever;
    private Integer taskID;
    private String message;

    public BLNotification(Integer taskID , String sender , String reciever , String message)
    {
        this.taskID = taskID;
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
    }

    public BLNotification(NotificationDTO notification)
    {
        this.taskID = notification.getTaskID();
        this.sender = notification.getSender();
        this.reciever = notification.getReciever();
        this.message = notification.getMessage();
    }

    public NotificationDTO toDTO()
    {
        return new NotificationDTO(this.taskID , this.sender , this.reciever , this.message);
    }

    public String getSender()
    {
        return this.sender;
    }

    public String getReciever()
    {
        return this.reciever;
    }

    public Integer getTaskID()
    {
        return this.taskID;
    }

    public String getMessage()
    {
        return this.message;
    }

    @Override
    public String toString()
    {
        return this.sender.toString() + " invited " + this.reciever.toString() + " to " + this.taskID;
    }
}
