package nikesnkrsaccountgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class App {
    public static void main( String[] args ) {
        String countryCode;
        String fileName = "accounts.txt";
        WebDriver driver = new FirefoxDriver();
        ProtonmailGenerator emailGen = new ProtonmailGenerator(driver);
        SnkrsAccountGen nikeGen = new SnkrsAccountGen(driver);
        Scanner sc = new Scanner(System.in);

        System.setProperty("webdriver.gecko.driver","geckodriver.exe");

        System.out.println("  _   _ _____ _  ________     _____ _   _ _  _______   _____     _____ ______ _   _ ______ _____         _______ ____  _____");
        System.out.println(" | \\ | |_   _| |/ /  ____|   / ____| \\ | | |/ /  __ \\ / ____|   / ____|  ____| \\ | |  ____|  __ \\     /\\|__   __/ __ \\|  __ \\");
        System.out.println(" |  \\| | | | | ' /| |__     | (___ |  \\| | ' /| |__) | (___    | |  __| |__  |  \\| | |__  | |__) |   /  \\  | | | |  | | |__) |");
        System.out.println(" | . ` | | | |  < |  __|     \\___ \\| . ` |  < |  _  / \\___ \\   | | |_ |  __| | . ` |  __| |  _  /   / /\\ \\ | | | |  | |  _  / ");
        System.out.println(" | |\\  |_| |_| . \\| |____    ____) | |\\  | . \\| | \\ \\ ____) |  | |__| | |____| |\\  | |____| | \\ \\  / ____ \\| | | |__| | | \\ \\ ");
        System.out.println(" |_| \\_|_____|_|\\_\\______|  |_____/|_| \\_|_|\\_\\_|  \\_\\_____/    \\_____|______|_| \\_|______|_|  \\_\\/_/    \\_\\_|  \\____/|_|  \\_\\");
        System.out.println("\n\nDeveloped by panda2k");
        System.out.print("\n\nWould you like to generate a UK SNKRS account or CN SNKRS account? ");
        countryCode = sc.nextLine();
        countryCode = countryCode.toLowerCase();

        Email accountEmail = emailGen.generateAccount();
        System.out.println("Protonmail Account generated. Here are the details: " + accountEmail.toString());
        Email account = nikeGen.generateAccount(countryCode, accountEmail);

        System.out.println("SNKRS Account generated. Here are the details: " + account.toString());

        try {
            FileWriter textWriter = new FileWriter(new File(fileName), true);
            System.out.println("Now writing account to file");
            textWriter.write(account.toString() + "\n");
            System.out.println("Finished writing account to file");
            textWriter.close();
        } 
        catch (IOException e) {
            System.out.println("caught io exception");
        }

        sc.close();
        driver.quit();
    }
}
