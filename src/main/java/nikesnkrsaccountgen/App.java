package nikesnkrsaccountgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class App {
    public static void main( String[] args ) {
        // the generator is going to work best if using a vpn/proxy located in Asia or the UK depending on your account verification option
        String countryCode;
        int accountCount;
        String fileName;
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
        System.out.print("\nHow many " + countryCode.toUpperCase() + " verified accounts would you like to create? ");
        accountCount = sc.nextInt();
        sc.nextLine();
        System.out.print("\nSpecify the file name you would like these accounts to be saved under: ");
        fileName = sc.nextLine();
        fileName += ".txt";
        //System.out.println("Would you like to run the generator in headless mode? true or false"); todo
        //headless = sc.nextBoolean(); TODO

        Email[] accountList = new Email[accountCount];


        for(int count = 0; count < accountCount; count++) {
            System.out.println("Now generating account");
            accountList[count] = generateSnkrsAccount(countryCode);
            writeAccountToFile(new File(fileName), accountList[count]);

            System.out.println("Finished generating account");
        }

        System.out.println("Finished generating all accounts");

        sc.close();
    }
    
    public static Email generateSnkrsAccount(String countryCode) {
        System.out.println("Creating driver and generator objects");
        WebDriver driver = new FirefoxDriver();
        ProtonmailGenerator emailGen = new ProtonmailGenerator(driver);
        SnkrsAccountGen nikeGen = new SnkrsAccountGen(driver);

        Email accountEmail = emailGen.generateAccount();
        System.out.println("Protonmail Account generated. Here are the details: " + accountEmail.toString());
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