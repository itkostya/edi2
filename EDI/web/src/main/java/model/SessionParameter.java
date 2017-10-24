package model;

import categories.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;


/*
 * Created by kostya on 9/12/2016.
 */
public enum SessionParameter {

    INSTANCE;

    private final Map<String, UserSettings> activeUsers = new TreeMap<>();

    public User getCurrentUser(HttpServletRequest req) {
        return activeUsers.get(req.getRequestedSessionId()).getUser();
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

}
