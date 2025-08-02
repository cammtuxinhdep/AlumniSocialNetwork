package com.vmct.controllers;

import com.vmct.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public String statsView(
            @RequestParam(name = "type", defaultValue = "month") String type,
            @RequestParam(name = "year", defaultValue = "2025") int year,
            Model model) {

        model.addAttribute("type", type);
        model.addAttribute("year", year);

        model.addAttribute("userStats", statsService.countUsersByTime(type.toUpperCase(), year));
        model.addAttribute("postStats", statsService.countPostsByTime(type.toUpperCase(), year));

        return "stats";
    }
}
