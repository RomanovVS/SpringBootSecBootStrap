package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/")
    public String redirecting() {
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<User> listUser = userService.showAllUsers();
        User user = getLoggedUser();
        String listRoles = user.getListRoles();
        model.addAttribute("users", listUser);
        model.addAttribute("mail", user.getEmail());
        model.addAttribute("role", listRoles);
        model.addAttribute("currentUser", user);
        return "admin";
    }

    @GetMapping(value = "/user")
    public String UserRole(Model model) {
        User user = getLoggedUser();
        String listRoles = user.getListRoles();
        model.addAttribute("user", user);
        model.addAttribute("role", listRoles);
        return "user";
    }

    @GetMapping(value = "/admin/add")
    public String UserAddGetForm(Model model) {
        User user = getLoggedUser();
        String roles = user.getListRoles();
        model.addAttribute("currentUser", user);
        model.addAttribute("role", roles);
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", userService.showAllRoles());
        return "add";
    }

    @PostMapping(value = "/admin/add")
    public String AddUserPostForm(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/edit")
    public String EditUserEdit(@RequestParam("id") Long id, @RequestParam("rolesList") List<Role> rolesList, @ModelAttribute("user") User user) {
        System.out.println(user);
        System.out.println(id);
        System.out.println(rolesList.toString());
        user.setRoles(rolesList);
        userService.editUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete")
    public String delete(@RequestParam String id) {
        System.out.println(id);
        userService.deleteUser(Long.valueOf(id));
        return "redirect:/admin";
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameUser = auth.getName();
        return userService.findByEmail(nameUser);
    }
}