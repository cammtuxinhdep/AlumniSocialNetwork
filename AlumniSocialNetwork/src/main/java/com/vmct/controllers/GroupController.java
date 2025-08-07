package com.vmct.controllers;

import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import com.vmct.services.UserGroupService;
import com.vmct.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;


    @GetMapping
    public String listGroups(Model model) {
        model.addAttribute("groups", userGroupService.getAllGroups());
        return "group-list";
    }

@GetMapping("/form")
public String groupForm(@RequestParam(name = "id", required = false) Long id, Model model) {
    UserGroup group = (id != null) ? userGroupService.getGroupById(id) : new UserGroup();

    model.addAttribute("group", group);


    model.addAttribute("allUsers", userService.getAllUsers());

    List<User> groupUsers = (id != null) ? userGroupService.getUsersByGroupIds(List.of(id)) : List.of();
    model.addAttribute("groupUsers", groupUsers);

    return "group-form";
}

    @PostMapping("/save")
    public String saveGroup(@ModelAttribute("group") @Valid UserGroup group,
                            BindingResult result,
                            @RequestParam(value = "memberIds", required = false) List<Long> memberIds,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allUsers", userService.getAllUsers());
            return "group/form";
        }

        userGroupService.saveGroup(group, memberIds);
        return "redirect:/group";
    }
    @GetMapping("/delete")
    public String deleteGroup(@RequestParam("id") Long id) {
        userGroupService.deleteGroup(id);
        return "redirect:/group";
    }
}
