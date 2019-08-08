package nikesnkrsaccountgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class App {
    public static void main( String[] args ) {
        System.setProperty("webdriver.gecko.driver","geckodriver.exe");
        //String fileName = "accounts_" + 
        GmailGen gen = new GmailGen();
        try {
            PrintWriter textWriter = new PrintWriter(new File("accounts.txt"));
            String[][] emailArray = gen.generateGmail();
            textWriter.append(emailArray[0][0]);
            textWriter.append(emailArray[0][1]);
        } 
        catch (FileNotFoundException e) {
            System.out.println("accounts.txt file not found");
        }
    }
}
