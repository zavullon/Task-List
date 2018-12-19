package project.data;

import project.logic.DTO.TaskDTO;
import project.logic.DTO.UserDTO;
import project.util.Request;

import java.util.List;

public interface RelationsDAO
{
    public List<Integer> getTaskIDsByUser(UserDTO user , Request request);

    public List<Integer> getTaskIDsByUser(UserDTO user);

    public List<String> getUserLoginsByTask(TaskDTO task);

    public void add(TaskDTO task , UserDTO user);

    public void delete(TaskDTO task , UserDTO user);

    public void delete(TaskDTO task);

    public void delete(UserDTO user);
}
