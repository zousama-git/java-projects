package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates personalized health coaching text based on user profile
 * and calculated metrics. All output is coach-voiced — supportive, specific.
 */
public class Coach {

    // tip banks
    private static final String[] LOSE_TIPS = {
        "Walk 8,000–10,000 steps/day — no gym needed. It adds up fast.",
        "Swap sugary drinks for water or sparkling water with lemon.",
        "Eat protein first at every meal to feel fuller, longer.",
        "Sleep 7–9 hours. Poor sleep raises hunger hormones (ghrelin).",
        "Try intermittent fasting (16:8) — skip breakfast, eat noon–8 PM.",
        "Cook at home 5x/week — restaurant meals have hidden calories.",
        "Fill half your plate with vegetables before anything else.",
        "Track your food for just 3 days — awareness is a superpower."
    };

    private static final String[] GAIN_TIPS = {
        "Eat in a 300–500 calorie surplus — slow gains keep fat low.",
        "Prioritise compound lifts: squats, deadlifts, bench, rows.",
        "Hit 1.6–2.2 g of protein per kg of bodyweight daily.",
        "Sleep is when your muscles grow — protect those 8 hours.",
        "Add a protein shake post-workout if hitting protein is tough.",
        "Progress slowly: add 2.5–5 kg to bar every 1–2 weeks.",
        "Creatine monohydrate (3–5 g/day) is the most proven supplement.",
        "Eat 4–6 meals/day to distribute protein synthesis optimally."
    };

    private static final String[] MAINTAIN_TIPS = {
        "Consistency beats perfection — aim for 80% adherence.",
        "Build healthy habits: daily walks, weekly strength training.",
        "Track weight weekly (same time, same conditions) for trends.",
        "Focus on strength gains — muscle mass protects long-term health.",
        "Plan your meals Sunday — it dramatically reduces poor choices.",
        "Keep healthy snacks visible; hide junk food out of sight.",
        "Hydrate well: aim for ~35 ml per kg bodyweight daily.",
        "Annual bloodwork is the ultimate health check-up."
    };

    private static final String[] ACTIVE_TIPS = {
        "Recovery is training too — schedule 1–2 full rest days.",
        "Periodise your training: easy weeks every 4th week.",
        "Track heart-rate variability (HRV) to gauge recovery state.",
        "Mobility work (10 min/day) prevents injuries down the line."
    };

    private static final String[] SEDENTARY_TIPS = {
        "The easiest win: stand up and walk 5 min every hour.",
        "Start with 20-minute walks — sustainable beats heroic.",
        "Bodyweight workouts at home require zero equipment.",
        "Consider a standing desk to break prolonged sitting."
    };

    // public API
    /** Returns a list of 4 personalised tips for this profile. */
    public static List<String> tips(UserProfile p, double bmi) {
        List<String> tips = new ArrayList<>();
        String[] primary = MAINTAIN_TIPS;
        if      ("lose".equals(p.goal)) primary = LOSE_TIPS;
        else if ("gain".equals(p.goal)) primary = GAIN_TIPS;

        // pick 3 from goal-specific pool
        for (int i = 0; i < 3 && i < primary.length; i++) tips.add(primary[i]);

        // pick 1 from activity-level pool
        if ("sedentary".equals(p.activityLevel) || "light".equals(p.activityLevel)) {
            tips.add(SEDENTARY_TIPS[0]);
        } else {
            tips.add(ACTIVE_TIPS[0]);
        }
        return tips;
    }

    /** Opening coach line tailored to BMI & goal. */
    public static String openingLine(UserProfile p, double bmi) {
        String name = capitalize(p.username);
        if (bmi < 18.5) {
            return name + ", your body needs more fuel. Let's build you up safely.";
        }
        if (bmi < 25.0 && "maintain".equals(p.goal)) {
            return name + ", you're in the healthy zone — let's keep you there!";
        }
        if (bmi < 25.0 && "lose".equals(p.goal)) {
            return "Great progress, " + name + "! You're already in a healthy range.";
        }
        if (bmi < 25.0 && "gain".equals(p.goal)) {
            return name + ", healthy BMI is the perfect launchpad for muscle-building.";
        }
        if (bmi < 30.0) {
            return name + ", you're close to the healthy range — small steps create big change.";
        }
        return name + ", every journey starts with a single step. You've already taken it.";
    }

    /** Milestone message based on history trend. */
    public static String milestone(UserProfile p) {
        int n = p.history.size();
        if (n == 0) return null;
        if (p.checkInStreak >= 3) {
            return "🔥 " + p.checkInStreak + "-check-in streak! Consistency is your superpower.";
        }
        if (n >= 2) {
            double first = p.history.get(0).bmi;
            double last  = p.history.get(n-1).bmi;
            double delta = last - first;
            if (delta < -2.0) return "🎉 Amazing — you've dropped " + String.format("%.1f", Math.abs(delta)) + " BMI points since you started!";
            if (delta < -0.5) return "📉 Great trend! BMI is moving in the right direction.";
            if (delta >  1.0 && "gain".equals(p.goal)) return "📈 Solid progress — you're building mass nicely!";
        }
        return null;
    }

    /** Projection sentence for X kg change. */
    public static String project(UserProfile p, double deltaKg, double heightM) {
        double projBmi = HealthEngine.projectedBmi(p.weightKg, deltaKg, heightM);
        String cat = HealthEngine.bmiCategory(projBmi);
        String direction = deltaKg < 0 ? "lose" : "gain";
        return String.format("If you %s %.0f kg → BMI becomes %.1f (%s)",
            direction, Math.abs(deltaKg), projBmi, cat);
    }

    /** Future-pace: if pace continues, where will user be in 3 months? */
    public static String futureYou(UserProfile p) {
        int n = p.history.size();
        if (n < 2) return null;
        double firstKg = p.history.get(0).weightKg;
        double lastKg  = p.history.get(n-1).weightKg;
        double months  = n; // each entry ~ 1 month
        double ratePerMonth = (lastKg - firstKg) / months;
        double futureKg = lastKg + (ratePerMonth * 3);
        double futureBmi = HealthEngine.bmi(futureKg, p.heightM);
        String cat = HealthEngine.bmiCategory(futureBmi);
        return String.format(
            "At your current pace, in 3 months you could weigh ~%.1f kg (BMI %.1f — %s).",
            futureKg, futureBmi, cat);
    }

    // private helpers
    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
