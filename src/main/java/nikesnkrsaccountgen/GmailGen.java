package nikesnkrsaccountgen;

import java.util.Random;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GmailGen extends AccountGenerator{
    WebDriver driver;
    final int startingTypeSpeed = 75;


    public GmailGen(WebDriver driver) {
        this.driver = driver;
    }

    public void generateGmail(String proxy) {
    // write later
        Proxy gmailProxy = new Proxy();
        gmailProxy.setHttpProxy(proxy);
    }

    public Email generateGmail() {
        Email newGmail;

        Random randomGen = new Random();
        String emailEnding = randomNumberString(10);
        String password = randomPassword(10);
        String phoneNumber = getPhoneNumber("uk", "1");
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
        
        newGmail = new Email(firstName + lastName.charAt(0) + emailEnding + "@gmail.com", password);
        newGmail.setFirstName(getFirstName());
        newGmail.setLastName(getLastName());

        waiter.until(ExpectedConditions.elementToBeClickable(By.id("phoneNumberId")));
        typeKeys(phoneNumber, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("phoneNumberId"))); 
        driver.findElement(By.xpath("//*[contains(text(), 'Next')]")).click();

        // get the verification code for gmail account
        verificationMessage = getMessage(phoneNumber, "uk", "1");
        for(int count = 2; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
            if(Character.isDigit(verificationMessage.charAt(count))) {
                verificationCode += verificationMessage.charAt(count);
            }
        }
        
        System.out.println("Verification code:" + verificationCode);
        
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("code"))); 
        typeKeys(verificationCode, randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("code"))); 
        driver.findElement(By.id("gradsIdvVerifyNext")).click();
        blacklistNumber(phoneNumber, "uk", "1");

        waiter.until(ExpectedConditions.elementToBeClickable(By.id("month"))); 
        enterRandomInfo(newGmail);
        driver.findElement(By.id("personalDetailsNext")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("phoneUsageNext"))); 
        driver.findElement(By.id("phoneUsageNext")).click();
        System.out.println("Almost finished created google account.\nEmail is " + newGmail.getEmailAddress() + "\nPassword is: " + newGmail.getPassword());
        
        // remove later
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Error while pausing to check terms of service");
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

    public Email generateFakeEmail() {
        Email newGmail;
        String firstName = getFirstName();
        String lastName = getLastName();
        String password = randomPassword(10);
        Random randomGen = new Random();
        String emailEnding;

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

        newGmail = new Email(firstName + lastName.charAt(0) + emailEnding, password);
        fillEmailProfile(newGmail);

        return newGmail;
    }

    private void enterRandomInfo(Email newGmail) {
    /**
    * Generates random information for a google login for
    * only to be used when the driver is on the google information screen
    */
        Random randomGen = new Random();
        GregorianCalendar birthday = new GregorianCalendar(randomGen.nextInt(20) + 1970, randomGen.nextInt(12) + 1, randomGen.nextInt(28) + 1);
        Select month = new Select(driver.findElement(By.id("month")));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping during account information fillout");
        }
        month.selectByValue(Integer.toString(birthday.get(Calendar.MONTH)));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping during account information fillout");
        }
        typeKeys(Integer.toString(birthday.get(Calendar.DAY_OF_MONTH)), randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("day")));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping during account information fillout");
        }
        typeKeys(Integer.toString(birthday.get(Calendar.YEAR)), randomGen.nextInt(11) + startingTypeSpeed, driver.findElement(By.id("year")));

        Select gender = new Select(driver.findElement(By.id("gender")));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping during account information fillout");
        }
        newGmail.setGender(randomGen.nextInt(2) + 1);
        gender.selectByValue(Integer.toString(newGmail.getGender()));

        newGmail.setDateOfBirth(birthday);
    }
}
