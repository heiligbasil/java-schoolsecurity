package com.lambdaschool.school;

import com.lambdaschool.school.model.Role;
import com.lambdaschool.school.model.User;
import com.lambdaschool.school.model.UserRoles;
import com.lambdaschool.school.service.RoleService;
import com.lambdaschool.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Component
public class SeedData implements CommandLineRunner
{
    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Override
    public void run(String[] args) throws Exception
    {
        Role r1 = new Role("users");

        roleService.save(r1);

        // user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r1));
        User u1 = new User("user", "password", users);
        userService.save(u1);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(), r1));
        User u2 = new User("lambda", "school", users);
        userService.save(u2);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(), r1));
        User u3 = new User("jack", "isback", users);
        userService.save(u3);
    }
}