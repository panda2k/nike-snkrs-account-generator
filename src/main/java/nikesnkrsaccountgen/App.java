package nikesnkrsaccountgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class App {
    public static void main( String[] args ) {
        String countryCode;
        int accountCount;
        String fileName;
        Boolean headless;
        Boolean logging;
        Boolean realEmail;
        Scanner sc = new Scanner(System.in);

        System.setProperty("webdriver.gecko.driver","geckodriver.exe");

        System.out.println("  _   _ _____ _  ________     _____ _   _ _  _______   _____     _____ ______ _   _ ______ _____         _______ ____  _____");
        System.out.println(" | \\ | |_   _| |/ /  ____|   / ____| \\ | | |/ /  __ \\ / ____|   / ____|  ____| \\ | |  ____|  __ \\     /\\|__   __/ __ \\|  __ \\");
        System.out.println(" |  \\| | | | | ' /| |__     | (___ |  \\| | ' /| |__) | (___    | |  __| |__  |  \\| | |__  | |__) |   /  \\  | | | |  | | |__) |");
        System.out.println(" | . ` | | | |  < |  __|     \\___ \\| . ` |  < |  _  / \\___ \\   | | |_ |  __| | . ` |  __| |  _  /   / /\\ \\ | | | |  | |  _  / ");
        System.out.println(" | |\\  |_| |_| . \\| |____    ____) | |\\  | . \\| | \\ \\ ____) |  | |__| | |____| |\\  | |____| | \\ \\  / ____ \\| | | |__| | | \\ \\ ");
        System.out.println(" |_| \\_|_____|_|\\_\\______|  |_____/|_| \\_|_|\\_\\_|  \\_\\_____/    \\_____|______|_| \\_|______|_|  \\_\\/_/    \\_\\_|  \\____/|_|  \\_\\");
        System.out.println("\n\nDeveloped by panda2k");
        System.out.println("This generator is going to work best if you're using a VPN or proxy that changes your ip location " + 
                            "to the same location as the phone number you're verifying with. For example, use a UK VPN/proxy if you're generating UK verified accounts");
        System.out.print("\n\nWould you like to generate a UK SNKRS account or CN SNKRS account? ");
        countryCode = sc.nextLine();
        countryCode = countryCode.toLowerCase();
        System.out.print("\nHow many " + countryCode.toUpperCase() + " verified accounts would you like to create? ");
        accountCount = sc.nextInt();
        sc.nextLine();
        System.out.print("\nSpecify the file name you would like these accounts to be saved under: ");
        fileName = sc.nextLine();
        fileName += ".txt";
        System.out.print("\nWould you like to run the generator in headless mode? true or false "); 
        headless = sc.nextBoolean();
        System.out.print("\nWould you like to log driver messages to console? true or false ");
        logging = sc.nextBoolean();
        System.out.print("\nWould you like to generate real emails for each account? true or false ");
        realEmail = sc.nextBoolean();

        Email[] accountList = new Email[accountCount];


        for(int count = 0; count < accountCount; count++) {
            System.out.println("Now generating account");
            accountList[count] = generateSnkrsAccount(countryCode, headless, logging, realEmail);
            writeAccountToFile(new File(fileName), accountList[count]);

            System.out.println("Finished generating account");
        }

        System.out.println("Finished generating all accounts");

        sc.close();
    }
    
    public static Email generateSnkrsAccount(String countryCode, boolean headless, boolean logging, boolean realEmail) {
        WebDriver driver = null;
        Email accountEmail = null;

        System.out.println("Creating driver and generator objects");
        if(headless) {
            System.out.println("Entering headless mode");
            FirefoxBinary binary = new FirefoxBinary();
            binary.addCommandLineOptions("--headless");

            FirefoxOptions options = new FirefoxOptions();
            options.setBinary(binary);
            if(logging == false) {
                System.out.println("Disabling logging");
                System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"log.txt");
            }

            driver = new FirefoxDriver(options);
        }
        else {
            if(logging == false) {
                System.out.println("Disabling logging");
                System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"log.txt");
            }
            driver = new FirefoxDriver();
        }

        if(realEmail) {
            ProtonmailGenerator emailGen = new ProtonmailGenerator(driver);
            accountEmail = emailGen.generateAccount();
        }
        else {
            GmailGen emailGen = new GmailGen(driver);
            accountEmail = emailGen.generateFakeEmail();
        }
        System.out.println("Email generated: " + accountEmail.toString());

        SnkrsAccountGen nikeGen = new SnkrsAccountGen(driver);

        Email account = nikeGen.generateAccount(countryCode, accountEmail);

        System.out.println("SNKRS Account generated. Here are the details: " + account.toString());
        driver.quit();

        return account;
    }

    public static void writeAccountToFile(File outputFile, Email account) {
        FileWriter textWriter = null;

        try {
            textWriter = new FileWriter(outputFile, true);
        } 
        catch (IOException e) {
            System.out.println("Caught IOException while creating FileWriter");
        }

        System.out.println("Now writing account to file");

        try {
            textWriter.write(account.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Caught IOException when writing an account to file.");
        }

        try {
            textWriter.close();
            System.out.println("Finished closing FileWriter");
        } catch (IOException e) {
            System.out.println("Caught IOException while closing FileWriter");
        }
        System.out.println("Finished writing account to file");
    }
}