package com.vmct.services;
import com.vmct.pojo.UserGroup;
import java.util.List;

public interface UserGroupService {
    List<UserGroup> getAllGroups();
    UserGroup getGroupById(Long id);
}
