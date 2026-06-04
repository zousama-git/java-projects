package util;

/**
 * Terminal color/style constants and pretty-print helpers.
 * All ANSI escapes are behind a single flag so the app can run
 * in plain-text mode if the terminal doesn't support color.
 */
public final class Term {

    // ANSI reset
    public static final String RESET  = "\u001B[0m";

    // text colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // bright variants
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // background colors
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_MAGENTA = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    // styles
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";

    // box-drawing chars
    public static final String H  = "─"; // horizontal
    public static final String V  = "│"; // vertical
    public static final String TL = "╔"; // top-left
    public static final String TR = "╗"; // top-right
    public static final String BL = "╚"; // bottom-left
    public static final String BR = "╝"; // bottom-right
    public static final String ML = "╠"; // middle-left
    public static final String MR = "╣"; // middle-right
    public static final String MH = "═"; // middle-horizontal

    private Term() {}

    /** Repeat a string n times. */
    public static String rep(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    /** Print a wide banner box with centred title. */
    public static void banner(String title, String color) {
        int width = 58;
        String top    = TL + rep(MH, width) + TR;
        String bottom = BL + rep(MH, width) + BR;
        int pad = (width - title.length()) / 2;
        String mid = V + rep(" ", pad) + title + rep(" ", width - pad - title.length()) + V;
        System.out.println(color + BOLD + top);
        System.out.println(mid);
        System.out.println(bottom + RESET);
    }

    /** Print a thin separator line. */
    public static void separator(String color) {
        System.out.println(color + rep("─", 60) + RESET);
    }

    /** Print a section header. */
    public static void section(String label) {
        System.out.println("\n" + BOLD + BRIGHT_CYAN + "  ▸ " + label + RESET);
        System.out.println(CYAN + "  " + rep("─", 50) + RESET);
    }

    /** Colour-coded BMI bar visual. */
    public static void bmiBar(double bmi) {
        System.out.println();
        System.out.println(BOLD + "  BMI SCALE" + RESET);
        System.out.println(
            "  " + BRIGHT_BLUE  + "Underweight" + RESET + " │ " +
            BRIGHT_GREEN + "  Healthy  " + RESET + " │ " +
            BRIGHT_YELLOW + " Overweight" + RESET + " │ " +
            BRIGHT_RED + "   Obese   " + RESET
        );
        System.out.println(
            "  " + BLUE  + "  < 18.5   " + RESET + " │ " +
            GREEN + " 18.5–24.9 " + RESET + " │ " +
            YELLOW + " 25.0–29.9 " + RESET + " │ " +
            RED + "  30.0+    " + RESET
        );

        // build pointer row
        // zones: 0-10 (UW), 11-21 (N), 22-32 (OW), 33-43 (OB)
        // we map bmi 10→45 onto 0→43 positions
        double clamp = Math.max(10.0, Math.min(bmi, 45.0));
        int pos = (int) ((clamp - 10.0) / (45.0 - 10.0) * 43);
        StringBuilder pointer = new StringBuilder("  ");
        for (int i = 0; i < 46; i++) {
            if (i == 10 || i == 22 || i == 33) pointer.append("│");
            else if (i == pos) pointer.append("^");
            else pointer.append(" ");
        }
        System.out.println(BOLD + BRIGHT_WHITE + pointer + RESET);
        System.out.println();
    }

    /** ASCII progress bar [████░░░░] with colour. */
    public static String progressBar(double value, double max, int width, String fillColor) {
        int filled = (int) Math.round((value / max) * width);
        filled = Math.max(0, Math.min(filled, width));
        return "[" + fillColor + rep("█", filled) + DIM + rep("░", width - filled) + RESET + "]";
    }

    /** Prints a labelled progress bar with percentage. */
    public static void labelledBar(String label, double value, double max, int width, String color) {
        double pct = (value / max) * 100.0;
        pct = Math.min(pct, 100.0);
        System.out.printf("  %-20s %s %5.1f%%%n",
            label,
            progressBar(value, max, width, color),
            pct);
    }

    /** Short pause to give a "typing" feel between sections. */
    public static void pause(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    /** Print a step indicator like [2/5]. */
    public static void step(int current, int total, String label) {
        System.out.printf("%n%s%s[%d/%d]%s %s%s%n",
            BOLD, BRIGHT_MAGENTA, current, total, RESET, BRIGHT_WHITE, label);
        System.out.println(MAGENTA + "  " + rep("·", 50) + RESET);
    }

    /** Prints a coloured inline tag e.g. ● HEALTHY */
    public static void tag(String text, String color) {
        System.out.print(BOLD + color + " ● " + text + RESET + "  ");
    }

    /** Clear screen (works on most terminals). */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
