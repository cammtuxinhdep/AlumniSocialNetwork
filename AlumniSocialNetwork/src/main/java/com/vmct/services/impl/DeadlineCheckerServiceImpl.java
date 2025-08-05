/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.services.impl;

import com.vmct.pojo.User;
import com.vmct.repositories.UserRepository;
import com.vmct.services.DeadlineCheckerService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
@Transactional
public class DeadlineCheckerServiceImpl implements DeadlineCheckerService {

    @Autowired
    private UserRepository userRepo;
    
    @Scheduled(cron = "0 0 * * * *") // Check mỗi giờ
    @Override
    public void checkPasswordChangeStatus() {
        Date now = new Date();
        List<User> lecturers = this.userRepo.getUncheckedLecturers();
        
        for(User lecturer : lecturers) {
            if (now.after(lecturer.getPasswordChangeDeadline())) {
                lecturer.setIsLocked(Boolean.TRUE);
                this.userRepo.updateUser(lecturer);
            }
        }
    }
}
