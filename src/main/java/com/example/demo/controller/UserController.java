package com.example.demo.controller;

import com.example.demo.service.ManagerService;
import com.example.demo.service.UserService;
import com.example.demo.vo.Manager;
import com.example.demo.vo.Team;
import com.example.demo.vo.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Getter
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired private UserService userService;
    @Autowired private ManagerService managerService;

    // @GetMapping("/new")
    // public User createNewProfile(@RequestParam String name, @RequestParam String firstname, @RequestParam String mail,
    //                             @RequestParam Team workTeam) {
    //
    //     User u = this.userService.saveUser(
    //             User.builder().name(name).firstname(firstname).mail(mail).workTeam(workTeam).build());
    //
    //     return u;
    // }

    @GetMapping("/tempnew")
    public User createNewProfile(@RequestParam String name, @RequestParam String firstname, @RequestParam String mail,
                                 @RequestParam boolean isManager) {
        User u;
        if (isManager) u = this.userService.saveUser(User.builder().name(name).firstname(firstname).mail(mail).build());
        else u = this.userService.saveUser(Manager.builder().name(name).firstname(firstname).mail(mail).build());
        //else u = this.managerService.saveManager(Manager.builder().name(name).firstname(firstname).mail(mail).build());
        return u;
    }

    @GetMapping("/delete")
    public HttpStatus deleteUser(@RequestParam int id) {

        return this.userService.deleteUser(id);
    }

    @GetMapping("/is-manager")
    public boolean isManager(@RequestParam int id) {
        return this.userService.getUser(id) instanceof Manager;
    }

    @GetMapping("/list")
    public List<User> getUserList() {
        return this.userService.getUserList();
    }
}
