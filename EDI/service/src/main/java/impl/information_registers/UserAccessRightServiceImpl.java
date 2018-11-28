package impl.information_registers;

import categories.User;
import hibernate.impl.information_registers.UserAccessRightImpl;
import information_registers.UserAccessRight;

import java.util.List;

public enum UserAccessRightServiceImpl {

    INSTANCE;

    public List<UserAccessRight> getUserRights(User user){
        return UserAccessRightImpl.INSTANCE.getUserRights(user);
    }

    public boolean setUserRights(User user, List<UserAccessRight> userAccessRights) {
        return UserAccessRightImpl.INSTANCE.setUserRights(user, userAccessRights);
    }
}
