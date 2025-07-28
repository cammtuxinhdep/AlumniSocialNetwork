package com.vmct.repositories;

import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import java.util.List;

public interface UserGroupRepository {
    List<UserGroup> getAllGroups();
    UserGroup getGroupById(Long id);
    List<User> getUsersByGroupIds(List<Long> groupIds);
}
