package util;

import java.util.LinkedHashMap;
import java.util.Map;

/** Stores all user profiles in memory and tracks the current session. */
public class UserDatabase {

    private final Map<String, UserProfile> users = new LinkedHashMap<>();
    private UserProfile current = null;   // null → guest session

    public UserDatabase() {
        // seed accounts
        UserProfile alice = new UserProfile("alice", "pass123");
        alice.age = 28;
        alice.gender = "Female";
        alice.activityLevel = "moderate";
        alice.goal = "lose";
        alice.weightKg = 72.0;
        alice.heightM = 1.65;
        alice.waistCm = 82.0;
        // inject some fake history so the tracker screen has data
        alice.history.add(new HealthEntry("Jan", 76.0, 1.65, 27.9));
        alice.history.add(new HealthEntry("Feb", 74.5, 1.65, 27.4));
        alice.history.add(new HealthEntry("Mar", 72.0, 1.65, 26.4));
        alice.totalCheckIns = 3;
        alice.checkInStreak = 3;

        UserProfile bob = new UserProfile("bob", "qwerty");
        bob.age = 35;
        bob.gender = "Male";
        bob.activityLevel = "active";
        bob.goal = "gain";
        bob.weightKg = 68.0;
        bob.heightM = 1.80;
        bob.waistCm = 78.0;
        bob.history.add(new HealthEntry("Jan", 66.5, 1.80, 20.5));
        bob.history.add(new HealthEntry("Feb", 67.2, 1.80, 20.7));
        bob.history.add(new HealthEntry("Mar", 68.0, 1.80, 21.0));
        bob.totalCheckIns = 3;
        bob.checkInStreak = 3;

        UserProfile charlie = new UserProfile("charlie", "hello99");
        charlie.age = 42;
        charlie.gender = "Male";
        charlie.activityLevel = "sedentary";
        charlie.goal = "lose";
        charlie.weightKg = 95.0;
        charlie.heightM = 1.75;
        charlie.waistCm = 102.0;
        charlie.history.add(new HealthEntry("Jan", 98.0, 1.75, 32.0));
        charlie.history.add(new HealthEntry("Feb", 96.5, 1.75, 31.5));
        charlie.history.add(new HealthEntry("Mar", 95.0, 1.75, 31.0));
        charlie.totalCheckIns = 3;
        charlie.checkInStreak = 3;

        users.put("alice", alice);
        users.put("bob", bob);
        users.put("charlie", charlie);
        users.put("diana", new UserProfile("diana","sunshine"));
        users.put("eve", new UserProfile("eve","qwerty123"));
    }

    // auth

    public boolean login(String username, String password) {
        UserProfile p = users.get(username);
        if (p != null && p.password.equals(password)) {
            current = p;
            return true;
        }
        return false;
    }

    public void logout() { current = null; }

    public boolean isLoggedIn() { return current != null; }

    public UserProfile getCurrent() { return current; }

    /** Display name: username if logged in, "Guest" otherwise. */
    public String displayName() {
        return current != null ? current.username : "Guest";
    }

    /** Register a brand-new account. Returns false if username taken. */
    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new UserProfile(username, password));
        current = users.get(username);
        return true;
    }

    /** Checks if a username already exists. */
    public boolean exists(String username) {
        return users.containsKey(username);
    }
}
