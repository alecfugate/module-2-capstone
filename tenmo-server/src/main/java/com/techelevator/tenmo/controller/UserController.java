package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping(path="/users")
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @GetMapping(path="/users/{id}")
    public User getUserByUserId(@PathVariable int id) {
        return userDao.getUserById(id);
    }

}
