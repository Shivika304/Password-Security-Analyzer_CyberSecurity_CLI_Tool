
import java.io.*;
import java.util.*;

class PasswordAnalyzer{
   static  Set<String> commonPass=new HashSet<>();
    public static void loadData(){
        try{
        BufferedReader br=new BufferedReader(new FileReader("rockyou.txt"));
        String line;
        while((line= br.readLine())!=null){
            commonPass.add(line.trim());
        }
        br.close();
        }
         catch(Exception e){
            System.out.println("File Not Found");
         }

    }
    static int calculateCharset(String pass){
        int score=0;
        if(pass.matches(".*[A-Z].*")) score+=26;
        if(pass.matches(".*[a-z].*")) score+=26;
        if(pass.matches(".*[0-9].*")) score+=10;
        if(pass.matches(".*[!@#$()&^%$].*")) score+=32;
        return score;
    }
    static void suggestImprovements(String pass){

    System.out.println("\nSuggestions to improve password:");

    if(pass.length() < 12)
        System.out.println("• Increase password length to at least 12 characters");

    if(!pass.matches(".*[A-Z].*"))
        System.out.println("• Add uppercase letters");

    if(!pass.matches(".*[a-z].*"))
        System.out.println("• Add lowercase letters");

    if(!pass.matches(".*[0-9].*"))
        System.out.println("• Add numbers");

    if(!pass.matches(".*[!@#$%^&*()].*"))
        System.out.println("• Add special characters (!@#$ etc)");
    }
    static String formatTime(double seconds){

    if(seconds < 60)
        return String.format("%.2f seconds", seconds);

    double minutes = seconds / 60;
    if(minutes < 60)
        return String.format("%.2f minutes", minutes);

    double hours = minutes / 60;
    if(hours < 24)
        return String.format("%.2f hours", hours);

    double days = hours / 24;
    if(days < 365)
        return String.format("%.2f days", days);

    double years = days / 365;
    return String.format("%.2f years", years);
}
    static double crackEntropy(String password){
        int charset=calculateCharset(password);
        int length=password.length();
        return length* (Math.log(charset)/Math.log(2));
    }
    static double estimateCrackTime(double entropy){
        double guess=Math.pow(2,entropy);
        double guessperSecond=1e9;
        return (guess/guessperSecond);
    }
    public static void main(String args[]){
        loadData();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Password");
        String pass=sc.nextLine();
        int score=0;
        if(pass.length()>=8) score++;
        if(pass.matches(".*[A-Z].*")) score++;
        if(pass.matches(".*[a-z].*")) score++;
        if(pass.matches(".*[0-9].*")) score++;
        if(pass.matches(".*[!@#$()&^%$].*")) score++;
        if(commonPass.contains(pass)){
            System.out.println("Password is extremely common!");
            System.out.println("Result: VERY WEAK");
            System.out.println("Estimated crack time: less than 1 second");
            return;
        }
        if(score<=2) System.out.println("Result: WEAK");
        else if(score<=4) System.out.println("Result: MEDIUM");
        else{
            System.out.println("Result: STRONG");
        }
        double entropy = crackEntropy(pass);
        double seconds = estimateCrackTime(entropy);

        System.out.printf("Entropy: %.2f bits%n", entropy);

        String readableTime = formatTime(seconds);
        System.out.println("Estimated crack time: " + readableTime);

        if(score <= 4){
            suggestImprovements(pass);
        }
    }
}