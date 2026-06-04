package util;

/** All health metric calculations. Pure functions, no I/O. */
public class HealthEngine {

    // BMI
    public static double bmi(double kg, double heightM) {
        return kg / (heightM * heightM);
    }

    public static String bmiCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal Weight";
        if (bmi < 27.5) return "Slightly Overweight";
        if (bmi < 30.0) return "Overweight";
        if (bmi < 35.0) return "Obese Class I";
        if (bmi < 40.0) return "Obese Class II";
        return "Obese Class III";
    }

    public static String bmiColor(double bmi) {
        if (bmi < 18.5) return Term.BRIGHT_BLUE;
        if (bmi < 25.0) return Term.BRIGHT_GREEN;
        if (bmi < 30.0) return Term.BRIGHT_YELLOW;
        return Term.BRIGHT_RED;
    }

    /** One-line health implication narrative. */
    public static String bmiNarrative(double bmi) {
        if (bmi < 16.0) return "This is critically low. Your body may lack essential nutrients for vital functions.";
        if (bmi < 18.5) return "You're underweight. A balanced, calorie-sufficient diet can restore healthy weight.";
        if (bmi < 22.0) return "Excellent! You're in a great spot — your weight supports long-term health well.";
        if (bmi < 25.0) return "You're in the healthy range. Maintaining this through activity & diet is your goal.";
        if (bmi < 27.5) return "Slightly above ideal. Small lifestyle adjustments can bring you back to optimal.";
        if (bmi < 30.0) return "At this range, risk for metabolic conditions is elevated but very reversible.";
        if (bmi < 35.0) return "Obesity Class I carries meaningful risk. Consistent habits will shift the trend.";
        if (bmi < 40.0) return "At this level, medical guidance alongside lifestyle change is strongly advisable.";
        return "This range poses serious health risks. A doctor-supported plan is highly recommended.";
    }

    // Estimated Body Fat % (US Navy / Deurenberg hybrid)
    public static double bodyFatPercent(double bmi, int age, boolean isMale) {
        // Deurenberg formula: BF% = (1.20 × BMI) + (0.23 × age) − (10.8 × sex) − 5.4
        // sex = 1 for male, 0 for female
        double sex = isMale ? 1.0 : 0.0;
        return (1.20 * bmi) + (0.23 * age) - (10.8 * sex) - 5.4;
    }

    public static String bodyFatCategory(double bf, boolean isMale) {
        if (isMale) {
            if (bf <  6) return "Essential fat only";
            if (bf < 14) return "Athletic";
            if (bf < 18) return "Fitness";
            if (bf < 25) return "Average";
            return "Above average";
        } else {
            if (bf < 14) return "Essential fat only";
            if (bf < 21) return "Athletic";
            if (bf < 25) return "Fitness";
            if (bf < 32) return "Average";
            return "Above average";
        }
    }

    // BMR & TDEE
    /** Mifflin-St Jeor BMR. */
    public static double bmr(double kg, double heightM, int age, boolean isMale) {
        double h = heightM * 100; // convert to cm
        if (isMale) return (10 * kg) + (6.25 * h) - (5 * age) + 5;
        else        return (10 * kg) + (6.25 * h) - (5 * age) - 161;
    }

    public static double tdee(double bmr, double activityMultiplier) {
        return bmr * activityMultiplier;
    }

    /** Calories for goal: lose (-500), maintain (0), gain (+300). */
    public static double targetCalories(double tdee, String goal) {
        if ("lose".equals(goal))     return tdee - 500;
        if ("gain".equals(goal))     return tdee + 300;
        return tdee;
    }

    // Waist-to-Height Ratio
    public static double waistToHeight(double waistCm, double heightM) {
        return waistCm / (heightM * 100);
    }

    public static String whrCategory(double whr) {
        if (whr < 0.40) return "Extremely Slim";
        if (whr < 0.50) return "Healthy";
        if (whr < 0.53) return "Healthy (upper boundary)";
        if (whr < 0.58) return "Overweight";
        if (whr < 0.63) return "Very Overweight";
        return "Obese";
    }

    public static String whrColor(double whr) {
        if (whr < 0.50) return Term.BRIGHT_GREEN;
        if (whr < 0.58) return Term.BRIGHT_YELLOW;
        return Term.BRIGHT_RED;
    }

    // Ideal Weight Range (Hamwi method)
    public static double idealWeightLow(double heightM, boolean isMale) {
        return bmiToKg(18.5, heightM);
    }

    public static double idealWeightHigh(double heightM, boolean isMale) {
        return bmiToKg(24.9, heightM);
    }

    private static double bmiToKg(double targetBmi, double heightM) {
        return targetBmi * heightM * heightM;
    }

    // Projection helpers
    /** Project BMI after changing weight by delta kg. */
    public static double projectedBmi(double currentKg, double deltaKg, double heightM) {
        return bmi(currentKg + deltaKg, heightM);
    }

    /** Weeks to reach a goal weight losing/gaining ~0.5 kg/week. */
    public static int weeksToGoal(double currentKg, double goalKg) {
        double diff = Math.abs(goalKg - currentKg);
        return (int) Math.ceil(diff / 0.5);
    }

    // Formatting helpers
    public static String kgToLbs(double kg) {
        return String.format("%.1f lbs", kg * 2.20462);
    }

    public static String mToCm(double m) {
        return String.format("%.0f cm", m * 100);
    }

    public static String mToFeetInches(double m) {
        double inches = m * 39.3701;
        int feet = (int)(inches / 12);
        double in = inches % 12;
        return String.format("%d ft %.1f in", feet, in);
    }
}
