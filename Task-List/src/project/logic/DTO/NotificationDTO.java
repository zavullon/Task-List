package project.logic.DTO;

public class NotificationDTO
{
    private String sender;
    private String reciever;
    private Integer taskID;
    private String message;

    public NotificationDTO(Integer taskID , String sender , String reciever , String message)
    {
        this.sender = sender;
        this.reciever = reciever;
        this.taskID = taskID;
        this.message = message;
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
