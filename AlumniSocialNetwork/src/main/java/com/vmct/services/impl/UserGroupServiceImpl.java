package com.vmct.services.impl;

import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import com.vmct.repositories.UserGroupRepository;
import com.vmct.services.UserGroupService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    @Override
    public List<User> getUsersByGroupIds(List<Long> groupIds) {
        return userGroupRepo.getUsersByGroupIds(groupIds);
    }

    @Override
    public void saveGroup(UserGroup group, List<Long> memberIds) {
        boolean isNew = (group.getId() == null);

        if (isNew) {
            userGroupRepo.createGroup(group);
        } else {
            userGroupRepo.updateGroup(group);
        }
 
        if (!isNew) {
            List<User> oldMembers = userGroupRepo.getUsersByGroupIds(List.of(group.getId()));
            for (User oldMember : oldMembers) {
                userGroupRepo.removeUserFromGroup(group.getId(), oldMember.getId());
            }
        }
        if (memberIds != null) {
            for (Long userId : memberIds) {
                userGroupRepo.addUserToGroup(group.getId(), userId);
            }
        }
    }

    @Override
    public void deleteGroup(Long id) {
        userGroupRepo.deleteGroup(id);
    }
}
