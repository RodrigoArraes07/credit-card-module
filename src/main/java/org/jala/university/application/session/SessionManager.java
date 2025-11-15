package org.jala.university.application.session;

import org.jala.university.application.dto.UserDto;

import java.util.UUID;
import java.util.prefs.Preferences;

public class SessionManager {
    private static SessionManager instance;
    private static final String PREF_USER_ID = "logged_user_id";
    private static final String PREF_REMEMBER_ME = "remember_me";

    private final Preferences preferences;
    private UserDto currentUser;

    private SessionManager() {
        preferences = Preferences.userNodeForPackage(SessionManager.class);
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void startSession(UserDto user, boolean rememberMe) {
        this.currentUser = user;

        if (rememberMe) {
            preferences.put(PREF_USER_ID, user.getId().toString());
            preferences.putBoolean(PREF_REMEMBER_ME, true);
        } else {
            clearSavedSession();
        }
    }

    public void endSession() {
        this.currentUser = null;
        clearSavedSession();
    }

    public boolean hasActiveSession() {
        return currentUser != null;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public UUID getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    public boolean hasSavedSession() {
        return preferences.getBoolean(PREF_REMEMBER_ME, false)
            && preferences.get(PREF_USER_ID, null) != null;
    }

    public UUID getSavedUserId() {
        String userIdStr = preferences.get(PREF_USER_ID, null);
        if (userIdStr != null) {
            try {
                return UUID.fromString(userIdStr);
            } catch (IllegalArgumentException e) {
                clearSavedSession();
                return null;
            }
        }
        return null;
    }

    public void clearSavedSession() {
        preferences.remove(PREF_USER_ID);
        preferences.remove(PREF_REMEMBER_ME);
    }

    public void setCurrentUser(UserDto user) {
        this.currentUser = user;
    }
}
