package project.data;

import project.logic.DTO.UserDTO;

import java.util.List;

public interface UserDAO
{
    public UserDTO get(UserDTO user);

    public UserDTO getByLogin(String login);

    public List<UserDTO> getAll();

    public boolean add(UserDTO user);

    public void delete(UserDTO user);

    public void update(UserDTO oldUser , UserDTO newUser);
}
