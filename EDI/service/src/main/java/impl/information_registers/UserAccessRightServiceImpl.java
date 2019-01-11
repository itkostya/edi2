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

    public void setUserRights(List<UserAccessRight> userAccessRights) {
        UserAccessRightImpl.INSTANCE.setUserRights(userAccessRights);
    }
}
