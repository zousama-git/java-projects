package util;

import java.util.List;

/**
 * Renders every screen in the app.
 * All printing lives here so the flow controller stays clean.
 */
public class Screens {

    // splash
    public static void splash() {
        System.out.println();
        System.out.println(Term.BOLD + Term.BRIGHT_CYAN +
            "  ██╗  ██╗███████╗ █████╗ ██╗  ████████╗██╗  ██╗" + Term.RESET);
        System.out.println(Term.BOLD + Term.BRIGHT_CYAN +
            "  ██║  ██║██╔════╝██╔══██╗██║  ╚══██╔══╝██║  ██║" + Term.RESET);
        System.out.println(Term.BOLD + Term.CYAN +
            "  ███████║█████╗  ███████║██║     ██║   ███████║" + Term.RESET);
        System.out.println(Term.BOLD + Term.CYAN +
            "  ██╔══██║██╔══╝  ██╔══██║██║     ██║   ██╔══██║" + Term.RESET);
        System.out.println(Term.BOLD + Term.BRIGHT_BLUE +
            "  ██║  ██║███████╗██║  ██║███████╗██║   ██║  ██║" + Term.RESET);
        System.out.println(Term.BOLD + Term.BRIGHT_BLUE +
            "  ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═╝   ╚═╝  ╚═╝" + Term.RESET);
        System.out.println();
        System.out.println(Term.BOLD + Term.BRIGHT_WHITE +
            "           C O A C H   —   Your Smart Health Partner" + Term.RESET);
        System.out.println(Term.DIM + Term.WHITE +
            "                    Powered by real science." + Term.RESET);
        System.out.println();
        Term.separator(Term.CYAN);
    }

    // access menu
    public static void accessMenu() {
        System.out.println(Term.BOLD + Term.BRIGHT_WHITE + "\n  ACCESS" + Term.RESET);
        System.out.println(Term.DIM + "  Who are you today?" + Term.RESET);
        System.out.println();
        menuItem(1, "Log In","access your profile & history");
        menuItem(2, "Create Account","new here? let's set you up");
        menuItem(3, "Continue as Guest","quick BMI check, no account needed");
        menuItem(4, "Exit","");
    }

    // main menu
    public static void mainMenu(String displayName, boolean loggedIn) {
        Term.separator(Term.CYAN);
        System.out.printf("%n  %s%sMAIN MENU%s   %s(session: %s)%s%n",
            Term.BOLD, Term.BRIGHT_WHITE, Term.RESET,
            Term.DIM,  displayName,       Term.RESET);
        System.out.println();
        menuItem(1, "BMI Health Check", "full dashboard with coach insights");
        menuItem(2, "My Progress","history, trends & milestones");
        menuItem(3, "Health Tips","personalised advice for your goal");
        menuItem(4, "Future Projections","simulate weight changes & pace");
        if (loggedIn) {
            menuItem(5, "Log Out","");
            menuItem(6, "Exit","");
        } else {
            menuItem(5, "Log In","unlock profile features");
            menuItem(6, "Exit","");
        }
    }

    //login screen
    public static void loginScreen() {
        Term.section("LOGIN");
        System.out.println(Term.DIM + "  Type 'cancel' as username to go back." + Term.RESET);
        System.out.println();
    }

    // register screen
    public static void registerScreen() {
        Term.section("CREATE ACCOUNT");
        System.out.println(Term.DIM + "  Choose a username and password to get started." + Term.RESET);
        System.out.println();
    }

    // BMI guided flow
    public static void bmiIntro(String name) {
        Term.section("BMI HEALTH CHECK");
        System.out.printf("  %sHello, %s!%s Let's build your health dashboard.%n",
            Term.BOLD + Term.BRIGHT_WHITE, name, Term.RESET);
        System.out.println(Term.DIM + "  Input is always in metric (kg / cm)." + Term.RESET);
        System.out.println();
        Term.pause(300);
    }

    public static void bmiDashboard(
            UserProfile p, // may be null for guests
            String name,
            int age,
            double weightKg,
            double heightM,
            double waistCm,
            boolean isMale,
            boolean useImperial) {

        double bmi = HealthEngine.bmi(weightKg, heightM);
        double bf = HealthEngine.bodyFatPercent(bmi, age, isMale);
        double bmrVal = HealthEngine.bmr(weightKg, heightM, age, isMale);
        double actMult = (p != null) ? p.activityMultiplier() : 1.55;
        double tdee = HealthEngine.tdee(bmrVal, actMult);
        String goal = (p != null && p.goal != null) ? p.goal : "maintain";
        double targetCal = HealthEngine.targetCalories(tdee, goal);

        String bmiColor = HealthEngine.bmiColor(bmi);
        String bmiCat = HealthEngine.bmiCategory(bmi);
        String narrative = HealthEngine.bmiNarrative(bmi);

        System.out.println();
        System.out.println(Term.BOLD + Term.BRIGHT_WHITE +
            "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                  HEALTH DASHBOARD                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" + Term.RESET);
        System.out.println();

        // identity row
        System.out.printf("  %-12s %s%-20s%s    Age: %d    %s%n",
            "Name:", Term.BRIGHT_WHITE + Term.BOLD, name, Term.RESET, age,
            isMale ? Term.BLUE + "♂ Male" + Term.RESET : Term.MAGENTA + "♀ Female" + Term.RESET);

        // measurements
        if (useImperial) {
            System.out.printf("  %-12s %s    %-12s %s%n",
                "Height:", Term.BRIGHT_WHITE + HealthEngine.mToFeetInches(heightM) + Term.RESET,
                "Weight:", Term.BRIGHT_WHITE + HealthEngine.kgToLbs(weightKg) + Term.RESET);
        } else {
            System.out.printf("  %-12s %s    %-12s %s%n",
                "Height:", Term.BRIGHT_WHITE + HealthEngine.mToCm(heightM) + Term.RESET,
                "Weight:", Term.BRIGHT_WHITE + String.format("%.1f kg", weightKg) + Term.RESET);
        }

        System.out.println();
        Term.separator(Term.CYAN);

        // BMI readout
        System.out.printf("%n  %-14s %s%s%.2f%s   %s● %s%s%n",
            "BMI:", Term.BOLD, bmiColor, bmi, Term.RESET,
            Term.BOLD + bmiColor, bmiCat, Term.RESET);

        Term.bmiBar(bmi);

        // narrative
        System.out.println(Term.DIM + "  " + narrative + Term.RESET);
        System.out.println();
        Term.separator(Term.CYAN);

        // mini dashboard
        System.out.println();
        System.out.println(Term.BOLD + Term.BRIGHT_WHITE + "  METRICS AT A GLANCE" + Term.RESET);
        System.out.println();

        // body fat
        String bfCat = HealthEngine.bodyFatCategory(bf, isMale);
        String bfColor = (bf < 25 && isMale) || (bf < 32 && !isMale) ? Term.BRIGHT_GREEN : Term.BRIGHT_YELLOW;
        System.out.printf("  %-22s %s%-8.1f%%%s  %s%s%s%n",
            "Est. Body Fat:",
            Term.BOLD + bfColor, bf, Term.RESET,
            Term.DIM, bfCat, Term.RESET);

        // waist-to-height
        if (waistCm > 0) {
            double whr = HealthEngine.waistToHeight(waistCm, heightM);
            String whrCat = HealthEngine.whrCategory(whr);
            String whrColor = HealthEngine.whrColor(whr);
            System.out.printf("  %-22s %s%-8.2f%s  %s%s%s%n",
                "Waist-to-Height Ratio:",
                Term.BOLD + whrColor, whr, Term.RESET,
                Term.DIM, whrCat, Term.RESET);
        }

        // calorie bars
        System.out.printf("  %-22s %s%.0f kcal/day%s%n",
            "BMR (base calories):", Term.BRIGHT_WHITE, bmrVal, Term.RESET);
        System.out.printf("  %-22s %s%.0f kcal/day%s%n",
            "TDEE (maintenance):", Term.BRIGHT_WHITE, tdee, Term.RESET);
        System.out.printf("  %-22s %s%.0f kcal/day%s  %s[target for %s goal]%s%n",
            "Your target:", Term.BRIGHT_GREEN + Term.BOLD, targetCal, Term.RESET,
            Term.DIM, goal, Term.RESET);

        System.out.println();
        Term.labelledBar("BMI progress", Math.min(bmi, 40), 40, 28,
            bmiColor);

        // ideal weight
        double idealLow  = HealthEngine.idealWeightLow(heightM, isMale);
        double idealHigh = HealthEngine.idealWeightHigh(heightM, isMale);
        System.out.printf("%n  Ideal weight range: %s%.1f – %.1f kg%s%n",
            Term.BRIGHT_GREEN + Term.BOLD, idealLow, idealHigh, Term.RESET);

        System.out.println();
        Term.separator(Term.CYAN);
    }

    // progress tracker
    public static void progressScreen(UserProfile p) {
        Term.section("MY PROGRESS");
        if (p == null || p.historySize() == 0) {
            System.out.println(Term.DIM + "  No history yet. Complete a BMI check to start tracking." + Term.RESET);
            return;
        }

        // header
        System.out.println();
        System.out.printf("  %s%-8s  %-10s  %-8s  %-16s  %s%s%n",
            Term.BOLD + Term.BRIGHT_WHITE,
            "Period", "Weight", "BMI", "Category", "Trend", Term.RESET);
        Term.separator(Term.CYAN);

        double prevBmi = -1;
        for (int i = 0; i < p.history.size(); i++) {
            HealthEntry e = p.history.get(i);
            String trend = "";
            if (prevBmi > 0) {
                double delta = e.bmi - prevBmi;
                if (delta < -0.3) trend = Term.BRIGHT_GREEN + " ↓ " + String.format("%.1f", delta) + Term.RESET;
                else if (delta >  0.3) trend = Term.BRIGHT_RED   + " ↑ +" + String.format("%.1f", delta) + Term.RESET;
                else trend = Term.DIM + " → stable" + Term.RESET;
            }
            String bmiColor = HealthEngine.bmiColor(e.bmi);
            System.out.printf("  %-8s  %-10s  %s%-8.1f%s  %-16s  %s%n",
                e.date,
                String.format("%.1f kg", e.weightKg),
                Term.BOLD + bmiColor, e.bmi, Term.RESET,
                HealthEngine.bmiCategory(e.bmi),
                trend);
            prevBmi = e.bmi;
        }

        System.out.println();
        // streak
        System.out.printf("  %s🔥 Check-in streak: %d   |   Total check-ins: %d%s%n",
            Term.BRIGHT_YELLOW + Term.BOLD,
            p.checkInStreak, p.totalCheckIns, Term.RESET);

        // milestone
        String ms = Coach.milestone(p);
        if (ms != null) {
            System.out.println();
            System.out.println("  " + Term.BRIGHT_GREEN + Term.BOLD + ms + Term.RESET);
        }

        // future you
        String fy = Coach.futureYou(p);
        if (fy != null) {
            System.out.println();
            System.out.println(Term.BRIGHT_CYAN + "  🔮 " + fy + Term.RESET);
        }

        System.out.println();
        Term.separator(Term.CYAN);
    }

    // health tips screen
    public static void tipsScreen(UserProfile p, double bmi) {
        Term.section("HEALTH TIPS  —  Personalised for You");
        System.out.println();

        if (p != null) {
            System.out.println(Term.BOLD + Term.BRIGHT_WHITE +
                "  " + Coach.openingLine(p, bmi) + Term.RESET);
            System.out.println();
        }

        List<String> tips = Coach.tips(
            p != null ? p : defaultGuest(), bmi);

        for (int i = 0; i < tips.size(); i++) {
            System.out.printf("  %s%d.%s %s%n",
                Term.BRIGHT_CYAN + Term.BOLD, (i+1), Term.RESET,
                tips.get(i));
            System.out.println();
        }

        Term.separator(Term.CYAN);
    }

    // projections screen
    public static void projectionsScreen(UserProfile p, double weightKg, double heightM, double bmi) {
        Term.section("FUTURE PROJECTIONS");
        System.out.println();

        // simulate -5, -2, 0, +2, +5
        double[] deltas = { -5, -3, -1, 0, 1, 3, 5 };
        System.out.printf("  %s%-20s  %-10s  %-18s%s%n",
            Term.BOLD + Term.BRIGHT_WHITE,
            "Scenario", "New BMI", "Category", Term.RESET);
        Term.separator(Term.CYAN);
        for (double d : deltas) {
            double nBmi = HealthEngine.projectedBmi(weightKg, d, heightM);
            String color = HealthEngine.bmiColor(nBmi);
            String label = d == 0 ? " (current)" : (d < 0 ? String.format(" (lose %.0f kg)", Math.abs(d)) : String.format(" (gain %.0f kg)", d));
            System.out.printf("  %-20s  %s%-10.1f%s  %s%n",
                label.trim(),
                Term.BOLD + color, nBmi, Term.RESET,
                color + HealthEngine.bmiCategory(nBmi) + Term.RESET);
        }

        System.out.println();
        // time-to-goal
        double idealMid = (HealthEngine.idealWeightLow(heightM, true) + HealthEngine.idealWeightHigh(heightM, true)) / 2;
        int weeks = HealthEngine.weeksToGoal(weightKg, idealMid);
        System.out.printf("  %sAt 0.5 kg/week, reaching ideal mid-range weight (%.1f kg) → ~%d weeks.%s%n",
            Term.BRIGHT_CYAN, idealMid, weeks, Term.RESET);

        // future-you from history
        if (p != null) {
            String fy = Coach.futureYou(p);
            if (fy != null) {
                System.out.println();
                System.out.println("  " + Term.BRIGHT_MAGENTA + Term.BOLD + "🔮 " + fy + Term.RESET);
            }
        }

        System.out.println();
        Term.separator(Term.CYAN);
    }

    // goodbye
    public static void goodbye(String name) {
        System.out.println();
        Term.separator(Term.CYAN);
        System.out.printf("  %sGoodbye, %s!%s%n", Term.BOLD + Term.BRIGHT_WHITE, name, Term.RESET);
        System.out.println("  " + Term.DIM + "Stay healthy. See you next time. 💙" + Term.RESET);
        Term.separator(Term.CYAN);
        System.out.println();
    }

    // helpers
    private static void menuItem(int n, String label, String hint) {
        if (hint.isEmpty()) {
            System.out.printf("  %s[%d]%s  %s%n",
                Term.BOLD + Term.BRIGHT_CYAN, n, Term.RESET, label);
        } else {
            System.out.printf("  %s[%d]%s  %-22s %s%s%s%n",
                Term.BOLD + Term.BRIGHT_CYAN, n, Term.RESET,
                label,
                Term.DIM, hint, Term.RESET);
        }
    }

    private static UserProfile defaultGuest() {
        UserProfile g = new UserProfile("guest", "");
        g.goal          = "maintain";
        g.activityLevel = "moderate";
        return g;
    }
}
