package com.vmct.services.impl;

import com.vmct.pojo.UserGroup;
import com.vmct.repositories.UserGroupRepository;
import com.vmct.services.UserGroupService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl implements UserGroupService {
    @Autowired
    private UserGroupRepository userGroupRepo;

    @Override
    public List<UserGroup> getAllGroups() {
        return userGroupRepo.getAllGroups();
    }

    @Override
    public UserGroup getGroupById(Long id) {
        return userGroupRepo.getGroupById(id);
    }
}
