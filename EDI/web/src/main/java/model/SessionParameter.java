package model;

import categories.User;
import categories.UserRole;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;


/*
 * Created by kostya on 9/12/2016.
 */
public enum SessionParameter {

    INSTANCE;

    private final Map<String, UserSettings> activeUsers = new TreeMap<>();

    public User getCurrentUser(HttpServletRequest req) {
        UserSettings userSettings = activeUsers.get(req.getRequestedSessionId());
        return Objects.isNull(userSettings) ? null : userSettings.getUser();
    }

    public void setCurrentUser(HttpServletRequest req, User currentUser) {
        activeUsers.put(req.getRequestedSessionId(), new UserSettings(currentUser));
    }

    public UserSettings getUserSettings(HttpServletRequest req) {
        return activeUsers.get(req.getRequestedSessionId());
    }

    public boolean accessAllowed(HttpServletRequest req) {
        return activeUsers.containsKey(req.getRequestedSessionId());
    }

    public boolean adminAccessAllowed(HttpServletRequest req) {
        if (accessAllowed(req)){
            User currentUser = getCurrentUser(req);
            return (Objects.nonNull(currentUser) && currentUser.getRole() == UserRole.ADMIN);
        }
        return false;
    }

}
