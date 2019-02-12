package impl.information_registers;

import categories.User;
import enumerations.MetadataType;
import hibernate.impl.information_registers.UserAccessRightImpl;
import information_registers.UserAccessRight;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserAccessRightServiceImpl {

    INSTANCE;

    public void createDefaultUserRights(User user){

        List<UserAccessRight> metadataValues = Arrays.stream(MetadataType.values())
                .map(e -> new UserAccessRight(e, user, true, (e == MetadataType.MEMORANDUM || e == MetadataType.MESSAGE)))
                .collect(Collectors.toList());

        UserAccessRightImpl.INSTANCE.createUserRights(user, metadataValues);
    }

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

    public boolean viewAccess(User user, MetadataType metadataType){
        return UserAccessRightImpl.INSTANCE.viewAccess(user, metadataType);
    }

    public boolean editAccess(User user, MetadataType metadataType){
        return UserAccessRightImpl.INSTANCE.editAccess(user, metadataType);
    }

}
