package nikesnkrsaccountgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class App {
    public static void main( String[] args ) {
        System.setProperty("webdriver.gecko.driver","geckodriver.exe");
        //String fileName = "accounts_" + 
        Email newGmail = new Email("abc@simpleproxies.io", "Jumbobean123");
        
        //GmailGen gen = new GmailGen();
        SnkrsAccountGen nikeGen = new SnkrsAccountGen(newGmail);
        /*
        try {
            PrintWriter textWriter = new PrintWriter(new File("accounts.txt"));
            Gmail email = gen.generateGmail();
            textWriter.append(email.getEmailAddress());
            textWriter.append(email.getPassword());
        } 
        catch (FileNotFoundException e) {
            System.out.println("accounts.txt file not found");
        }
        */

        nikeGen.generateAccount();
    }
}
