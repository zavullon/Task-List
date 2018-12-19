package project.logic;

import project.data.NotificationsDAO;
import project.data.RelationsDAO;
import project.data.TaskDAO;
import project.data.UserDAO;
import project.logic.DTO.NotificationDTO;
import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;
import project.util.Date;
import project.util.Request;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleTaskManager implements TaskManager
{
    private final NotificationsDAO notificationsDAO;
    private final RelationsDAO relationsDAO;
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    public SimpleTaskManager(NotificationsDAO notificationsDAO , RelationsDAO relationsDAO , TaskDAO taskDAO , UserDAO userDAO)
    {
        this.notificationsDAO = notificationsDAO;
        this.relationsDAO = relationsDAO;
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<TaskDTO> getTasksBetween(UserDTO user , Date after , Date before)
    {
        return taskDAO.getAll()
                .stream()
                .filter((t) -> t.getDate().after(after) && t.getDate().before(before))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUntil(UserDTO user , Date date)
    {
        List<TaskDTO> userTasks = new LinkedList<>();
        relationsDAO.getTaskIDsByUser(user , new Request("added" , "done")).forEach((i) ->
        {
            TaskDTO task = taskDAO.getByID(i);
            if(task.getDate().before(date))
            {
                this.deleteTask(user , task);
            }
        });

    }

    @Override
    public void deleteTask(UserDTO user , TaskDTO task)
    {
        if(task.getCreatorLogin().equals(user.getLogin()))
        {
            relationsDAO.getUserLoginsByTask(task).forEach((l) -> notificationsDAO
                    .add(new NotificationDTO(task.getId() , task.getCreatorLogin() , l , "Task has been deleted")));
            relationsDAO.delete(task);
            notificationsDAO.delete(task);
            taskDAO.delete(task);
        }
        else
        {
            relationsDAO.delete(task , user);
            notificationsDAO.delete(task , user);
        }
    }

    @Override
    public void updateTask(TaskDTO oldTask , TaskDTO newTask)
    {
        relationsDAO.getUserLoginsByTask(oldTask).forEach((l) -> notificationsDAO
                .add(new NotificationDTO(oldTask.getId() , oldTask.getCreatorLogin() , l , "Changed")));
        taskDAO.update(oldTask , newTask);
    }

    @Override
    public void addTask(TaskDTO task)
    {
        taskDAO.add(task);
        relationsDAO.add(taskDAO.get(task) , userDAO.getByLogin(task.getCreatorLogin()));
    }

    @Override
    public TaskDTO getById(Integer id)
    {
        return taskDAO.getByID(id);
    }

    @Override
    public int getMaxID()
    {
        return taskDAO.getMaxID();
    }

    @Override
    public List<TaskDTO> getTasksByUser(UserDTO user , Request request)
    {
        return relationsDAO.getTaskIDsByUser(user , request).stream().map((i) -> taskDAO.getByID(i)).collect(Collectors.toList());
    }
}
