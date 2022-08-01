package ru.kata.spring.boot_security.demo.DAO;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    User getUserById(Long id);
    void editUser(Long id, User user);
    void deleteUser(Long id);
    List<User> showAllUsers();
    User findByEmail(String username);
}
