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

    public void updateUserRights(User user, List<UserAccessRight> userAccessRights) {
        checkTransformUserRights(userAccessRights);
        UserAccessRightImpl.INSTANCE.updateUserRights(user, userAccessRights);
    }

    public void checkTransformUserRights(List<UserAccessRight> userAccessRights){
        // Some combinations isn't available
        userAccessRights.stream().filter(f -> !f.isView() && f.isEdit()).forEach(userAccessRight -> userAccessRight.setView(true));
    }
}
