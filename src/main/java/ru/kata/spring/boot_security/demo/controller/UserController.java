package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameUser = auth.getName();
        User user = userService.findByEmail(nameUser);
        String listRoles = user.getListRoles();
        model.addAttribute("users", listUser);
        model.addAttribute("mail", user.getEmail());
        model.addAttribute("role", listRoles);
        model.addAttribute("currentUser", user);
        return "admin";
    }

    @GetMapping(value = "/user")
    public String UserRole(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameUser = auth.getName();
        User user1 = userService.findByEmail(nameUser);
        String listRoles = user1.getListRoles();
        model.addAttribute("user", user1);
        model.addAttribute("role", listRoles);
        return "user";
    }

    @GetMapping(value = "/admin/add")
    public String UserAddGetForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameUser = auth.getName();
        User user1 = userService.findByEmail(nameUser);
        String roles = user1.getListRoles();
        model.addAttribute("currentUser", user1);
        model.addAttribute("role", roles);
        return "add";
    }

    @PostMapping(value = "/admin/add")
    public String AddUserPostForm(@RequestParam("name") String name,
                                  @RequestParam("lastname") String lastname,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam("age") String age,
                                  @RequestParam("role") String role) {
        User user = new User(name, lastname, email, password, Integer.parseInt(age));
        user.setRolesOnForm(role);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete")
    public String delete(@RequestParam("id") String id) {
        userService.deleteUser(Long.parseLong(id));
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String UserEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "edit";
    }

    @PostMapping(value = "/admin/edit")
    public String EdUserEdit(@RequestParam("id") String id,
                             @RequestParam("name") String name,
                             @RequestParam("lastname") String lastname,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam("age") String age,
                             @RequestParam("role") String role) {
        User user = new User(name, lastname, email, password, Integer.parseInt(age));
        user.setId(Long.valueOf(id));
        user.setRolesOnForm(role);
        userService.editUser(Long.parseLong(id), user);
        return "redirect:/admin";
    }

}