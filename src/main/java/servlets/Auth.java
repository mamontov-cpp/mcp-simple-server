package servlets;


import java.util.HashMap;
import java.util.Map;

public class Auth {
    protected Map<String, Long> sessionsToUserIds;

    public Auth() {
        sessionsToUserIds = new HashMap<>();
    }

    public void authorize(String sess, long id) {
        sessionsToUserIds.put(sess, id);
    }

    public boolean isAuthorized(String sess) {
        return sessionsToUserIds.containsKey(sess);
    }
}
