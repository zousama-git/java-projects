package util;

import java.util.Scanner;
import java.util.Locale;

/** All console I/O with validation. Wraps a single shared Scanner. */
public class Input {

    private final Scanner sc;

    public Input() {
        this.sc = new Scanner(System.in);
        this.sc.useLocale(Locale.US);
    }

    // raw reads
    public String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    // validated numeric
    public double getDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                sc.nextLine();
                if (v >= min && v <= max) return v;
                System.out.printf(Term.BRIGHT_RED + "  ✖ Please enter a value between %.1f and %.1f.%n" + Term.RESET, min, max);
            } else {
                System.out.println(Term.BRIGHT_RED + "  ✖ That's not a number. Try again." + Term.RESET);
                sc.nextLine();
            }
        }
    }

    public int getInt(String prompt, int min, int max) {
        return (int) getDouble(prompt, min, max);
    }

    // menu choice
    public int menu(int min, int max) {
        return getInt(Term.BOLD + Term.BRIGHT_WHITE + "\n  Your choice → " + Term.RESET, min, max);
    }

    // yes / no
    public boolean yesNo(String prompt) {
        while (true) {
            System.out.print(prompt + Term.DIM + " [Y/N]: " + Term.RESET);
            String line = sc.nextLine().trim().toUpperCase();
            if (line.startsWith("Y")) return true;
            if (line.startsWith("N")) return false;
            System.out.println(Term.BRIGHT_RED + "  ✖ Please type Y or N." + Term.RESET);
        }
    }

    // credential reads
    public String getUsername() {
        System.out.print(Term.BRIGHT_CYAN + "  Username: " + Term.RESET);
        return sc.nextLine().trim().toLowerCase();
    }

    public String getPassword() {
        System.out.print(Term.BRIGHT_CYAN + "  Password: " + Term.RESET);
        return sc.nextLine().trim();
    }

    public void close() { sc.close(); }
}
