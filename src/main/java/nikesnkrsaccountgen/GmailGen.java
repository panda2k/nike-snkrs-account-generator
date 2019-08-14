package nikesnkrsaccountgen;

import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GmailGen {
    WebDriver driver;
    CloseableHttpClient requestsClient = HttpClients.createDefault();
    ResponseHandler errorHandler = new ResponseHandler<String>() {
        public String handleResponse(HttpResponse response) throws IOException{
            if(response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            }
            else {
                return "failed";
            }
        }
    };

    final String getSMSCodeAPIURL = "http://api.getsmscode.com/vndo.php";
    final String username = "littlwang@gmail.com";
    final String token = "36d0674c163d6b68ae2ccc89e461ed1f";

    private Gmail newGmail;

    public GmailGen() {
        driver = new FirefoxDriver();
    }

    public void generateGmail(String proxy) {
    // write later
        Proxy gmailProxy = new Proxy();
        gmailProxy.setHttpProxy(proxy);
    }

    public Gmail generateGmail() {
        String emailEnding = randomNumberString(10);
        String password = randomPassword(10);
        String phoneNumber = getPhoneNumber();
        String verificationMessage;
        String verificationCode = "";

        newGmail.setFirstName(getFirstName());
        newGmail.setLastName(getLastName());

        driver.get("https://accounts.google.com/SignUp");
        driver.findElement(By.name("firstName")).sendKeys(newGmail.getFirstName());
        driver.findElement(By.name("lastName")).sendKeys(newGmail.getLastName());
        driver.findElement(By.name("Passwd")).sendKeys(password);
        driver.findElement(By.name("ConfirmPasswd")).sendKeys(password);
        
        // keep trying email addresses until they are valid
        do {
            emailEnding = randomNumberString(10);
            driver.findElement(By.name("Username")).sendKeys(newGmail.getFirstName() + newGmail.getLastName().charAt(0) + emailEnding);
            driver.findElement(By.xpath("//*[contains(text(), 'Next')]")).click();
        } while(driver.findElements(By.xpath("//*[contains(text(), 'That username is taken. Try another.')]")).size() != 0);

        newGmail = new Gmail(newGmail.getFirstName() + newGmail.getLastName().charAt(0) + emailEnding + "@gmail.com", password);

        //phoneNumber = "2065663685"; // comment later
        driver.findElement(By.id("phoneNumberId")).clear();
        driver.findElement(By.id("phoneNumberId")).sendKeys(phoneNumber);
        driver.findElement(By.xpath("//*[contains(text(), 'Next')]")).click();

        // get the verification code for gmail account
        
        verificationMessage = getMessage(phoneNumber);
        for(int count = 2; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
            if(Character.isDigit(verificationMessage.charAt(count))) {
                System.out.println("Is digit");
                verificationCode += verificationMessage.charAt(count);
            }
        }
        
        System.out.println("Verification code:" + verificationCode);
        
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            System.out.println("error sleeping");
        }
        driver.findElement(By.id("code")).sendKeys(verificationCode);
        driver.findElement(By.id("gradsIdvVerifyNext")).click();

        System.out.println("Clicked");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("error sleeping");
        }
        enterRandomInfo();
        driver.findElement(By.id("personalDetailsNext")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("error sleeping");
        }
        if(driver.findElements(By.id("phoneUsageNext")).size() != 0) {
            System.out.println("Found extra form");
            driver.findElement(By.id("phoneUsageNext")).click();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("error sleeping");
        } 
        while(driver.findElements(By.xpath("//*[contains(text(), 'I agree')]")).size() == 0) {
            driver.findElement(By.xpath("//a[@role = 'button']")).click();
        }

        System.out.println("Finished created google account.\nEmail is " + newGmail.getEmailAddress() + "\nPassword is: " + newGmail.getPassword());
        return newGmail;
    }

    private String getPhoneNumber() {
        String getNumberURL = getSMSCodeAPIURL + "?action=getmobile&" + "username=" + username + "&token=" + token + "&pid=1" + "&cocode=uk"; 
        String phoneNumber = "+";
        HttpPost getSMSRequest = new HttpPost(getNumberURL);
        getSMSRequest.addHeader("Accept", "application/json");
        getSMSRequest.addHeader("Content-type", "application/json");

        System.out.println("Now executing API request " + getSMSRequest.getRequestLine());

        try {
            String response = requestsClient.execute(getSMSRequest, errorHandler);
            System.out.println("response: " + response);
            phoneNumber += response;
        } 
        catch (IOException e) {
            System.out.println("IOException. Please check URL parameters");
        }
            
        return phoneNumber;
    }

    private String getMessage(String phoneNumber) {
        String message = "Message|Not Receive"; 
        String getMessageURL = getSMSCodeAPIURL + "?action=getsms&username=" + username + "&token=" + token + "&pid=1&mobile=" + phoneNumber + "&cocode=uk"; 
        HttpPost getSMSRequest = new HttpPost(getMessageURL);
        getSMSRequest.addHeader("Accept", "application/json");
        getSMSRequest.addHeader("Content-type", "application/json");

        System.out.println("Now executing API request " + getSMSRequest.getRequestLine());
        do {
            try {
                String response = requestsClient.execute(getSMSRequest, errorHandler);
                System.out.println("response: " + response);
                message = response;
            } 
            catch (IOException e) {
                System.out.println("IOException. Please check URL parameters");
            }
    
            if(message.equals("Message|Not Receive") == true) {
                try {
                    Thread.sleep(10000);
                    System.out.println("No message recieved, sleeping for 10 seconds");                    
                } 
                catch (InterruptedException e) {
                    System.out.println("Unable to pause thread");
                }
            }
            
        } while(message.equals("Message|Not Receive") == true);
        System.out.println("Receieved message\nMessage:'" + message + "'");
        return message;
    }

    private String randomNumberString(int digits) {
        Random randomGen = new Random();
        String randomNumbers = "";

        for(int count = 0; count < digits; count++) {
            randomNumbers += randomGen.nextInt(10);
        }

        return randomNumbers;
    }

    private String randomPassword(int characterCount) {
        String password = "";
        Random randomGen = new Random();

        for(int count = 0; count < characterCount; count++) {
            password += (char) randomGen.nextInt(75) + 48;
        }

        return password;
    }

    private String getFirstName() {
        String firstName;
        ArrayList<String> firstNames = new ArrayList<String>();
        File firstNameList = new File("C:\\Users\\Michael Wang\\Programming\\nike-snkrs-account-generator\\resources\\first-names.txt");
        Random randomGen = new Random();

        try {
            Scanner sc = new Scanner(firstNameList);
            while(sc.hasNextLine()) {
                firstNames.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("First name list not found");
        }

        firstName = firstNames.get(randomGen.nextInt(firstNames.size()));

        return firstName;
    }

    private String getLastName() {
        String lastName;
        ArrayList<String> lastNames = new ArrayList<String>();
        File lastNameList = new File("C:\\Users\\Michael Wang\\Programming\\nike-snkrs-account-generator\\resources\\last-names.txt");
        Random randomGen = new Random();

        try {
            Scanner sc = new Scanner(lastNameList);
            while(sc.hasNextLine()) {
                lastNames.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Last name list not found");
        }

        lastName = lastNames.get(randomGen.nextInt(lastNames.size()));

        return lastName;
    }

    private void enterRandomInfo() {
    /**
    * Generates random information for a google login for
    * only to be used when the driver is on the google information screen
    */
        Random randomGen = new Random();
        GregorianCalendar birthday = new GregorianCalendar(randomGen.nextInt(20) + 1970, randomGen.nextInt(12) + 1, randomGen.nextInt(28) + 1);
        Select month = new Select(driver.findElement(By.id("month")));
        month.selectByValue(Integer.toString(birthday.get(Calendar.MONTH)));

        driver.findElement(By.id("day")).sendKeys(Integer.toString(birthday.get(Calendar.DAY_OF_MONTH)));
        driver.findElement(By.id("year")).sendKeys(Integer.toString(birthday.get(Calendar.YEAR)));

        Select gender = new Select(driver.findElement(By.id("gender")));
        newGmail.setGender(randomGen.nextInt(2) + 1);
        gender.selectByValue(Integer.toString(newGmail.getGender()));

        newGmail.setDateOfBirth(birthday);
    }
}
