package util;

import java.time.LocalDate;

/**
 * Main application controller.
 * Wires together Input, UserDatabase, Screens, HealthEngine, and Coach.
 */

public class AppController {

    private final Input in  = new Input();
    private final UserDatabase db  = new UserDatabase();

    // entry point
    public void run() {
        Screens.splash();
        Term.pause(200);
        accessFlow();
    }

    // access flow
    private void accessFlow() {
        while (true) {
            Screens.accessMenu();
            int choice = in.menu(1, 4);
            switch (choice) {
                case 1 -> { if (loginFlow()) mainMenuLoop(); }
                case 2 -> { if (registerFlow()) mainMenuLoop(); }
                case 3 -> mainMenuLoop();  // guest
                case 4 -> { Screens.goodbye("there"); in.close(); System.exit(0); }
            }
        }
    }

    // login
    private boolean loginFlow() {
        Screens.loginScreen();
        while (true) {
            String user = in.getUsername();
            if (user.equals("cancel")) return false;
            String pass = in.getPassword();
            if (db.login(user, pass)) {
                System.out.println();
                System.out.printf("  %s✔ Welcome back, %s!%s%n",
                    Term.BRIGHT_GREEN + Term.BOLD, user, Term.RESET);
                Term.pause(300);
                return true;
            }
            System.out.println(Term.BRIGHT_RED + "  ✖ Incorrect username or password." + Term.RESET);
        }
    }

    // register
    private boolean registerFlow() {
        Screens.registerScreen();
        while (true) {
            String user = in.getUsername();
            if (user.equals("cancel")) return false;
            if (db.exists(user)) {
                System.out.println(Term.BRIGHT_RED + "  ✖ Username already taken." + Term.RESET);
                continue;
            }
            String pass = in.getPassword();
            if (pass.length() < 4) {
                System.out.println(Term.BRIGHT_RED + "  ✖ Password must be at least 4 characters." + Term.RESET);
                continue;
            }
            db.register(user, pass);
            System.out.println(Term.BRIGHT_GREEN + Term.BOLD +
                "  ✔ Account created! Let's set up your profile." + Term.RESET);
            Term.pause(300);
            profileSetup();
            return true;
        }
    }

    // new-user profile setup
    private void profileSetup() {
        UserProfile p = db.getCurrent();
        if (p == null) return;

        Term.section("PROFILE SETUP  (you can skip by pressing Enter)");

        p.age    = in.getInt   ("  Age: ", 1, 120);
        System.out.println(Term.DIM + "  Gender [1=Male  2=Female  3=Other]: " + Term.RESET);
        int genderChoice = in.getInt("  → ", 1, 3);
        p.gender = switch (genderChoice) { case 1 -> "Male"; case 2 -> "Female"; default -> "Other"; };

        System.out.println(Term.DIM + "  Activity level:" + Term.RESET);
        System.out.println("    [1] Sedentary   [2] Lightly active   [3] Moderate   [4] Active   [5] Very active");
        int act = in.getInt("  → ", 1, 5);
        p.activityLevel = switch (act) {
            case 1 -> "sedentary"; case 2 -> "light"; case 3 -> "moderate";
            case 4 -> "active"; default -> "very_active";
        };

        System.out.println(Term.DIM + "  Goal:  [1] Lose weight   [2] Maintain   [3] Gain muscle" + Term.RESET);
        int goalChoice = in.getInt("  → ", 1, 3);
        p.goal = switch (goalChoice) { case 1 -> "lose"; case 2 -> "maintain"; default -> "gain"; };

        System.out.println(Term.BRIGHT_GREEN + "  ✔ Profile saved!" + Term.RESET);
    }

    // main menu loop
    private void mainMenuLoop() {
        while (true) {
            Screens.mainMenu(db.displayName(), db.isLoggedIn());
            int choice = in.menu(1, 6);
            switch (choice) {
                case 1 -> bmiFlow();
                case 2 -> progressFlow();
                case 3 -> tipsFlow();
                case 4 -> projectionsFlow();
                case 5 -> {
                    if (db.isLoggedIn()) {
                        db.logout();
                        System.out.println(Term.BRIGHT_YELLOW + "  Logged out." + Term.RESET);
                        accessFlow(); return;
                    } else {
                        if (loginFlow()) { /* stay in loop */ }
                    }
                }
                case 6 -> {
                    Screens.goodbye(db.displayName());
                    in.close();
                    System.exit(0);
                }
            }
        }
    }

    // BMI flow
    private void bmiFlow() {
        UserProfile p    = db.getCurrent();
        boolean isLoggedIn = db.isLoggedIn();

        // name
        String name;
        if (isLoggedIn) { name = p.username; }
        else { name = in.readLine("\n  " + Term.BRIGHT_CYAN + "Your name: " + Term.RESET); if (name.isEmpty()) name = "Guest"; }

        Screens.bmiIntro(name);

        // step 1, age
        Term.step(1, 5, "Age");
        int age;
        if (isLoggedIn && p.age > 0) {
            age = p.age;
            System.out.printf("  Using profile age: %s%d%s%n", Term.BRIGHT_WHITE, age, Term.RESET);
        } else {
            age = in.getInt("  Age (years): ", 1, 120);
        }

        // step 2, gender
        Term.step(2, 5, "Biological Sex (used for body-fat estimation)");
        boolean isMale;
        if (isLoggedIn && p.gender != null) {
            isMale = "Male".equals(p.gender);
            System.out.printf("  Using profile gender: %s%s%s%n", Term.BRIGHT_WHITE, p.gender, Term.RESET);
        } else {
            System.out.println("  [1] Male   [2] Female");
            isMale = (in.getInt("  → ", 1, 2) == 1);
        }

        // step 3, height
        Term.step(3, 5, "Height");
        System.out.println(Term.DIM + "  Enter in centimetres (e.g. 175 for 5'9\")" + Term.RESET);
        double heightM;
        if (isLoggedIn && p.heightM > 0) {
            heightM = p.heightM;
            System.out.printf("  Using profile height: %s%.0f cm%s%n",
                Term.BRIGHT_WHITE, heightM * 100, Term.RESET);
        } else {
            heightM = in.getDouble("  Height (cm): ", 50, 300) / 100.0;
        }

        // step 4,weight
        Term.step(4, 5, "Weight");
        double weightKg = in.getDouble("  Weight (kg): ", 10, 600);

        // step 5, waist (optional)
        Term.step(5, 5, "Waist circumference (optional — press 0 to skip)");
        System.out.println(Term.DIM + "  Measured at the navel, in cm." + Term.RESET);
        double waistCm = in.getDouble("  Waist (cm, 0 to skip): ", 0, 300);

        // save to profile history if logged in
        if (isLoggedIn) {
            String month = LocalDate.now().getMonth().toString().substring(0,3);
            p.waistCm = waistCm;
            double bmiVal = HealthEngine.bmi(weightKg, heightM);
            p.addEntry(month, weightKg, heightM, bmiVal);
        }

        // display loop, let user switch units
        while (true) {
            Term.pause(200);
            System.out.println();
            System.out.println(Term.DIM + "  Display units:  [1] Metric (kg/cm)   [2] Imperial (lbs/ft)" + Term.RESET);
            int unitChoice = in.getInt("  → ", 1, 2);
            boolean imperial = (unitChoice == 2);

            Screens.bmiDashboard(p, name, age, weightKg, heightM, waistCm, isMale, imperial);

            // coach section
            double bmi = HealthEngine.bmi(weightKg, heightM);
            if (isLoggedIn) {
                System.out.println(Term.BOLD + Term.BRIGHT_CYAN +
                    "\n  COACH SAYS" + Term.RESET);
                System.out.println("  " + Term.BRIGHT_WHITE +
                    Coach.openingLine(p, bmi) + Term.RESET);
                String ms = Coach.milestone(p);
                if (ms != null) System.out.println("  " + Term.BRIGHT_GREEN + ms + Term.RESET);
            }

            System.out.println();
            if (!in.yesNo("  View with different units?")) break;
        }
    }

    // progress flow
    private void progressFlow() {
        UserProfile p = db.getCurrent();
        Screens.progressScreen(p);
        if (p == null) {
            System.out.println(Term.DIM + "  Log in to save and view your history." + Term.RESET);
        }
        System.out.println(Term.DIM + "  Press Enter to continue…" + Term.RESET);
        in.readLine("");
    }

    // tips flow
    private void tipsFlow() {
        UserProfile p = db.getCurrent();
        double bmi = (p != null && p.weightKg > 0 && p.heightM > 0)
            ? HealthEngine.bmi(p.weightKg, p.heightM)
            : 25.0;  // default for guest
        Screens.tipsScreen(p, bmi);
        System.out.println(Term.DIM + "  Press Enter to continue…" + Term.RESET);
        in.readLine("");
    }

    // projections flow
    private void projectionsFlow() {
        UserProfile p = db.getCurrent();
        double weightKg, heightM;

        if (p != null && p.weightKg > 0 && p.heightM > 0) {
            weightKg = p.weightKg;
            heightM  = p.heightM;
        } else {
            System.out.println();
            weightKg = in.getDouble("  Current weight (kg): ", 10, 600);
            heightM  = in.getDouble("  Height (cm): ", 50, 300) / 100.0;
        }

        double bmi = HealthEngine.bmi(weightKg, heightM);
        Screens.projectionsScreen(p, weightKg, heightM, bmi);
        System.out.println(Term.DIM + "  Press Enter to continue…" + Term.RESET);
        in.readLine("");
    }
}