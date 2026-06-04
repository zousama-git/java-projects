package util;

import java.util.ArrayList;
import java.util.List;

/** A single logged health snapshot. */
class HealthEntry {
    final String date; // e.g. "2025-05"
    final double weightKg;
    final double heightM;
    final double bmi;

    HealthEntry(String date, double weightKg, double heightM, double bmi) {
        this.date = date;
        this.weightKg = weightKg;
        this.heightM = heightM;
        this.bmi = bmi;
    }
}

/** A full user profile stored in memory (no files needed for this demo). */
public class UserProfile {

    // identity
    final String username;
    final String password;

    // biographic
    int    age;
    String gender; // "Male" / "Female" / "Other"
    String activityLevel; // sedentary, light, moderate, active, very_active
    String goal; // lose, maintain, gain

    // current measurements
    double weightKg;
    double heightM;
    double waistCm;      // optional; 0 = not set

    // history
    final List<HealthEntry> history = new ArrayList<>();

    // gamification
    int checkInStreak = 0;
    int totalCheckIns = 0;

    public UserProfile(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** Returns how many entries the user has. */
    public int historySize() { return history.size(); }

    /** Adds a new entry; also bumps streak / checkin counter. */
    public void addEntry(String date, double weightKg, double heightM, double bmi) {
        history.add(new HealthEntry(date, weightKg, heightM, bmi));
        totalCheckIns++;
        checkInStreak++;
        // update current measurements
        this.weightKg = weightKg;
        this.heightM  = heightM;
    }

    /** Returns a display-friendly activity label. */
    public String activityLabel() {
        if (activityLevel == null) return "Unknown";
        switch (activityLevel) {
            case "sedentary": return "Sedentary (little/no exercise)";
            case "light":return "Lightly active (1-3 days/wk)";
            case "moderate": return "Moderately active (3-5 days/wk)";
            case "active": return "Very active (6-7 days/wk)";
            case "very_active": return "Extra active (athlete / physical job)";
            default: return activityLevel;
        }
    }

    /** Returns TDEE multiplier for the stored activity level. */
    public double activityMultiplier() {
        if (activityLevel == null) return 1.55;
        switch (activityLevel) {
            case "sedentary": return 1.2;
            case "light": return 1.375;
            case "moderate": return 1.55;
            case "active": return 1.725;
            case "very_active": return 1.9;
            default: return 1.55;
        }
    }
}
