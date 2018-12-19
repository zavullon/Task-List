package project.logic;

import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;
import project.util.Date;
import project.util.Request;

import java.util.List;

public interface TaskManager
{
    List<TaskDTO> getTasksBetween(UserDTO user , Date after , Date before);

    void deleteUntil(UserDTO user , Date date);

    void deleteTask(UserDTO user , TaskDTO task);

    void updateTask(TaskDTO oldTask , TaskDTO newTask);

    void addTask(TaskDTO task);

    List<TaskDTO> getTasksByUser(UserDTO user , Request request);

    TaskDTO getById(Integer id);

    int getMaxID();
}
