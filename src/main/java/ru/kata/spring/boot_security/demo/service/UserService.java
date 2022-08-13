package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    void saveUser(User user);
    User getUserById(Long id);
    void editUser(User user);
    void deleteUser(Long id);
    List<User> showAllUsers();
    List<Role> showAllRoles();
    User findByEmail(String username);
}