import java.util.Scanner;
import java.util.regex.Pattern;

public class PasswordAnalyzer {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your password: ");
//        password is a single sequence of characters, so use next().
        String password = sc.next();

        PasswordStrengthResult result = analyzePassword(password);
        System.out.println("Password Strength: " + result.strength);
        System.out.println("Score: " + result.score + "/5");
        if (result.recommendations.length > 0) {
            System.out.println("Recommendations:");
            for (String recommendation : result.recommendations) {
                System.out.println(" - " + recommendation);
            }
        }
    }

    static PasswordStrengthResult analyzePassword(String password) {
        // Criteria for a strong password
        boolean lengthCriteria = password.length() >= 8;
        boolean uppercaseCriteria = Pattern.compile("[A-Z]").matcher(password).find();
        boolean lowercaseCriteria = Pattern.compile("[a-z]").matcher(password).find();
        boolean digitCriteria = Pattern.compile("[0-9]").matcher(password).find();
        boolean specialCharCriteria = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find();

        // Password strength score
        int score = 0;
        if (lengthCriteria) score++;
        if (uppercaseCriteria) score++;
        if (lowercaseCriteria) score++;
        if (digitCriteria) score++;
        if (specialCharCriteria) score++;

        // Recommendations
        String[] recommendations = new String[5 - score];
        int recIndex = 0;
        if (!lengthCriteria) recommendations[recIndex++] = "Password should be at least 8 characters long.";
        if (!uppercaseCriteria) recommendations[recIndex++] = "Password should contain at least one uppercase letter.";
        if (!lowercaseCriteria) recommendations[recIndex++] = "Password should contain at least one lowercase letter.";
        if (!digitCriteria) recommendations[recIndex++] = "Password should contain at least one digit.";
        if (!specialCharCriteria) recommendations[recIndex++] = "Password should contain at least one special character.";

        // Strength classification
        String strength;
        if (score == 5) {
            strength = "Very Strong";
        } else if (score == 4) {
            strength = "Strong";
        } else if (score == 3) {
            strength = "Moderate";
        } else {
            strength = "Weak";
        }

        return new PasswordStrengthResult(strength, score, recommendations);
    }

    // Helper class to store the result
    static class PasswordStrengthResult {
        String strength;
        int score;
        String[] recommendations;

        PasswordStrengthResult(String strength, int score, String[] recommendations) {
            this.strength = strength;
            this.score = score;
            this.recommendations = recommendations;
        }
    }
}
