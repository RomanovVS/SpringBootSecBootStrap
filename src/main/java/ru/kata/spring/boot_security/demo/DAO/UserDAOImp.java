package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImp implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
        entityManager.flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> showAllUsers() {
        return (List<User>) entityManager.createQuery("SELECT user from User user").getResultList();
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> user = entityManager.createQuery("SELECT user from User user where user.email= :email", User.class);
        user.setParameter("email", email);
        return user.getSingleResult();
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
        entityManager.flush();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> showAllRoles() {
        return (List<Role>) entityManager.createQuery("SELECT role from Role role").getResultList();
    }
}