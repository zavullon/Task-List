package project.userinterface.entities;

import project.logic.DTO.NotificationDTO;

public class UINotification
{
    private String sender;
    private String reciever;
    private Integer taskID;
    private String message;
    private Boolean answer;

    public UINotification(Integer taskID , String sender , String reciever , String message)
    {
        this.taskID = taskID;
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
    }

    public UINotification(NotificationDTO notificationDTO)
    {
        this.taskID = notificationDTO.getTaskID();
        this.sender = notificationDTO.getSender();
        this.reciever = notificationDTO.getReciever();
        this.message = notificationDTO.getMessage();
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

    public Boolean getAnswer()
    {
        return this.answer;
    }

    public void setAnswer(Boolean answer)
    {
        this.answer = answer;
    }

    @Override
    public String toString()
    {
        return this.sender.toString() + " invited " + this.reciever.toString() + " to " + this.taskID;
    }
}
