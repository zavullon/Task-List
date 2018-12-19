package project.data;

import project.logic.DTO.TaskDTO;

import java.util.List;

public interface TaskDAO
{
    public List<TaskDTO> getAll();

    public TaskDTO get(TaskDTO task);

    public TaskDTO getByID(int id);

    public void delete(TaskDTO task);

    public void add(TaskDTO task);

    public void update(TaskDTO olTaskDTO , TaskDTO newTask);

    public int getMaxID();
}
