import java.io.*;
import java.util.*;

class PasswordAnalyzer {

    static Set<String> commonPass = new HashSet<>();

    // Load rockyou dataset
    public static void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader("rockyou.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                commonPass.add(line.trim().toLowerCase());
            }

        } catch (Exception e) {
            System.out.println("Dictionary file not found. Running without leaked password check.");
        }
    }

    // Calculate charset size
    static int calculateCharset(String pass) {

        int score = 0;

        if (pass.matches(".*[A-Z].*")) score += 26;
        if (pass.matches(".*[a-z].*")) score += 26;
        if (pass.matches(".*[0-9].*")) score += 10;
        if (pass.matches(".*[!@#$%^&*()].*")) score += 32;

        return score;
    }

    // Password entropy
    static double crackEntropy(String password) {

        int charset = calculateCharset(password);
        int length = password.length();

        if (charset == 0) return 0;

        return length * (Math.log(charset) / Math.log(2));
    }

    // Crack time estimation
    static double estimateCrackTime(double entropy) {

        double guesses = Math.pow(2, entropy);
        double guessesPerSecond = 1e9;

        return guesses / guessesPerSecond;
    }

    // Convert time into readable format
    static String formatTime(double seconds) {

        if (seconds < 1)
            return "Instantly crackable";

        if (seconds < 60)
            return String.format("%.2f seconds", seconds);

        double minutes = seconds / 60;
        if (minutes < 60)
            return String.format("%.2f minutes", minutes);

        double hours = minutes / 60;
        if (hours < 24)
            return String.format("%.2f hours", hours);

        double days = hours / 24;
        if (days < 365)
            return String.format("%.2f days", days);

        double years = days / 365;
        return String.format("%.2f years", years);
    }

    // Suggestions for improving password
    static void suggestImprovements(String pass) {

        System.out.println("\nSuggestions to improve password:");

        if (pass.length() < 12)
            System.out.println("• Increase password length to at least 12 characters");

        if (!pass.matches(".*[A-Z].*"))
            System.out.println("• Add uppercase letters");

        if (!pass.matches(".*[a-z].*"))
            System.out.println("• Add lowercase letters");

        if (!pass.matches(".*[0-9].*"))
            System.out.println("• Add numbers");

        if (!pass.matches(".*[!@#$%^&*()].*"))
            System.out.println("• Add special characters (!@#$ etc)");
    }

    // Strength meter UI
    static void showStrengthBar(int score) {

        String[] bars = {
                "[█░░░░] VERY WEAK",
                "[██░░░] WEAK",
                "[███░░] MEDIUM",
                "[████░] STRONG",
                "[█████] VERY STRONG"
        };

        if (score <= 1) System.out.println(bars[0]);
        else if (score == 2) System.out.println(bars[1]);
        else if (score == 3) System.out.println(bars[2]);
        else if (score == 4) System.out.println(bars[3]);
        else System.out.println(bars[4]);
    }

    // Detect repeated characters
    static boolean hasRepeatedCharacters(String pass) {

        int count = 1;

        for (int i = 1; i < pass.length(); i++) {

            if (pass.charAt(i) == pass.charAt(i - 1)) {
                count++;

                if (count >= 4)
                    return true;
            } else
                count = 1;
        }

        return false;
    }

    // Detect sequential patterns
    static boolean hasSequentialPattern(String pass) {

        String letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String lower = pass.toLowerCase();

        for (int i = 0; i < letters.length() - 3; i++) {
            if (lower.contains(letters.substring(i, i + 4)))
                return true;
        }

        for (int i = 0; i < numbers.length() - 3; i++) {
            if (lower.contains(numbers.substring(i, i + 4)))
                return true;
        }

        return false;
    }

    // Detect keyboard patterns
    static boolean hasKeyboardPattern(String pass) {

        String[] patterns = {
                "qwerty", "asdfgh", "zxcvbn",
                "qwertyuiop", "asdfghjkl"
        };

        String lower = pass.toLowerCase();

        for (String p : patterns) {
            if (lower.contains(p))
                return true;
        }

        return false;
    }

    // Normalize leetspeak
    static String normalizeLeet(String pass) {

        return pass.toLowerCase()
                .replace('@', 'a')
                .replace('0', 'o')
                .replace('1', 'i')
                .replace('3', 'e')
                .replace('$', 's');
    }

    public static void main(String[] args) {

        loadData();

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Password:");
        String pass = sc.nextLine();

        int score = 0;

        if (pass.length() >= 8) score++;
        if (pass.matches(".*[A-Z].*")) score++;
        if (pass.matches(".*[a-z].*")) score++;
        if (pass.matches(".*[0-9].*")) score++;
        if (pass.matches(".*[!@#$%^&*()].*")) score++;

        // Dictionary attack check
        if (commonPass.contains(pass.toLowerCase())) {

            System.out.println("\n⚠ SECURITY WARNING ⚠");
            System.out.println("Password found in leaked dataset!");
            System.out.println("Hackers can crack this instantly.");

            return;
        }

        // Leetspeak detection
        String normalized = normalizeLeet(pass);
        if (commonPass.contains(normalized)) {
            System.out.println("WARNING: Password resembles a common password using leetspeak.");
        }

        // Pattern detections
        if (hasRepeatedCharacters(pass))
            System.out.println("WARNING: Repeated character pattern detected.");

        if (hasSequentialPattern(pass))
            System.out.println("WARNING: Sequential pattern detected (abcd / 1234).");

        if (hasKeyboardPattern(pass))
            System.out.println("WARNING: Keyboard pattern detected (qwerty / asdf).");

        System.out.println("\nPassword Strength Analysis");
        System.out.println("--------------------------");

        showStrengthBar(score);

        double entropy = crackEntropy(pass);
        double seconds = estimateCrackTime(entropy);

        System.out.printf("\nEntropy: %.2f bits%n", entropy);
        System.out.println("Estimated crack time: " + formatTime(seconds));

        if (score <= 4)
            suggestImprovements(pass);
    }
}