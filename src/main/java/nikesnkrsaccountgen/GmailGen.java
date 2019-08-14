package nikesnkrsaccountgen;

import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
    final int startingTypeSpeed = 75;

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
        Random randomGen = new Random();
        String emailEnding = randomNumberString(10);
        String password = randomPassword(10);
        String phoneNumber = getPhoneNumber();
        String firstName = getFirstName();
        String lastName = getLastName();
        String verificationMessage;
        String verificationCode = "";
        WebDriverWait waiter = new WebDriverWait(driver, 10);

        driver.get("https://accounts.google.com/SignUp");

        typeKeys(firstName, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.name("firstName")));
        typeKeys(lastName, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.name("lastName")));
        typeKeys(password, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.name("Passwd")));
        typeKeys(password, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.name("ConfirmPasswd")));
        
        // keep trying email addresses until they are valid
        do {
            emailEnding = randomNumberString(10);
            typeKeys(firstName + lastName.charAt(0) + emailEnding, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.name("Username")));
            driver.findElement(By.xpath("//*[contains(text(), 'Next')]")).click();
        } while(driver.findElements(By.xpath("//*[contains(text(), 'That username is taken. Try another.')]")).size() != 0);
        
        newGmail = new Gmail(firstName + lastName.charAt(0) + emailEnding + "@gmail.com", password);
        newGmail.setFirstName(getFirstName());
        newGmail.setLastName(getLastName());

        waiter.until(ExpectedConditions.elementToBeClickable(By.id("phoneNumberId")));
        typeKeys(phoneNumber, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("phoneNumberId"))); 
        driver.findElement(By.xpath("//*[contains(text(), 'Next')]")).click();

        // get the verification code for gmail account
        verificationMessage = getMessage(phoneNumber);
        for(int count = 2; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
            if(Character.isDigit(verificationMessage.charAt(count))) {
                verificationCode += verificationMessage.charAt(count);
            }
        }
        
        System.out.println("Verification code:" + verificationCode);
        
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("code"))); 
        typeKeys(phoneNumber, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("code"))); 
        driver.findElement(By.id("gradsIdvVerifyNext")).click();

        waiter.until(ExpectedConditions.elementToBeClickable(By.id("month"))); 
        enterRandomInfo();
        driver.findElement(By.id("personalDetailsNext")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("phoneUsageNext"))); 
        driver.findElement(By.id("phoneUsageNext")).click();
        System.out.println("Almost finished created google account.\nEmail is " + newGmail.getEmailAddress() + "\nPassword is: " + newGmail.getPassword());
        // remove later
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //TODO: handle exception
        }
        System.out.println("size of button list " + driver.findElements(By.xpath("//button[@aria-label='Scroll down']")).size());
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Scroll down']")));
        while(driver.findElements(By.xpath("//*[contains(text(), 'I agree')]")).size() == 0) {
            driver.findElement(By.xpath("//button[@aria-label='Scroll down']")).click();
            System.out.println("Clicking scroll down");
        }
        driver.findElement(By.xpath("//*[contains(text(), 'I agree')]")).click();
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
        typeKeys(Integer.toString(birthday.get(Calendar.DAY_OF_MONTH)), randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("day")));
        typeKeys(Integer.toString(birthday.get(Calendar.YEAR)), randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("year")));

        Select gender = new Select(driver.findElement(By.id("gender")));
        newGmail.setGender(randomGen.nextInt(2) + 1);
        gender.selectByValue(Integer.toString(newGmail.getGender()));

        newGmail.setDateOfBirth(birthday);
    }

    private void typeKeys(String input, int wordsPerMinute, WebElement element) {
        for(int count = 0; count < input.length(); count++) {
            element.sendKeys(Character.toString(input.charAt(count)));
            try {
                Thread.sleep((int) (1000 / ((double) wordsPerMinute / 60)) / 5); // divide by 5 to account for average word length
            } 
            catch (InterruptedException e) {
                System.out.println("Error when trying to pause keystrokes.");
            }
        }
    }
}
