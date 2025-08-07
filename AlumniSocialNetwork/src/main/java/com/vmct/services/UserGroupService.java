package com.vmct.services;
import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import java.util.List;

public interface UserGroupService {
    List<UserGroup> getAllGroups();
    UserGroup getGroupById(Long id);
    List<User> getUsersByGroupIds(List<Long> groupIds);
     void saveGroup(UserGroup group, List<Long> memberIds);
    void deleteGroup(Long id);
}
    