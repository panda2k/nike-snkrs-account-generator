package nikesnkrsaccountgen;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProtonmailGenerator extends AccountGenerator{
    WebDriver driver;

    public ProtonmailGenerator() {
        driver = new FirefoxDriver();
    }

    public Email generateAccount() {
        WebDriverWait waiter = new WebDriverWait(driver, 15);
        Email newEmail;
        String firstName = getFirstName();
        String lastName = getLastName();
        String emailAddress = firstName + lastName.charAt(0) + randomNumberString(10);
        String password = randomPassword(10);
        String phoneNumber = getPhoneNumber("cn", "404");
        Actions builder = new Actions(driver);
        String verificationMessage;
        String verificationCode = "";
        
        driver.get("https://mail.protonmail.com/create/new?language=en");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Caught exception while sleeping");
        }
        System.out.println("Done sleeping");

        Actions typeUsername = builder.sendKeys(
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.TAB));
        typeUsername.perform();
        browserTypeKeys(builder, emailAddress, 75);
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Caught exception while sleeping");
        }

        if(driver.getPageSource().contains("Username already used")) {
            Actions fixEmail = builder.sendKeys(Keys.BACK_SPACE);
            fixEmail.perform();
        }

        typeKeys(password, 75, driver.findElement(By.id("password")));
        typeKeys(password, 75, driver.findElement(By.id("passwordc")));
        
        Actions enterAccount = builder.sendKeys(
        Keys.chord(Keys.TAB) + 
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.TAB) +
        Keys.chord(Keys.ENTER));
        enterAccount.perform();

        waiter.until(ExpectedConditions.elementToBeClickable(By.id("confirmModalBtn")));
        driver.findElement(By.id("confirmModalBtn")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='humanVerification-block-sms']//label[@class='signup-radio-label']"))).click();
        typeKeys(phoneNumber, 75, driver.findElement(By.id("smsVerification")));
        driver.findElement(By.xpath("//button[@class='pm_button primary codeVerificator-btn-send']")).click();

        verificationMessage = getMessage(phoneNumber.substring(1), "cn", "404");
        if(verificationMessage.equals("no message recieved")) {
            generateAccount();
        }
        for(int count = 1; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
            if(Character.isDigit(verificationMessage.charAt(count))) {
                verificationCode += verificationMessage.charAt(count);
            }
        }

        System.out.println("code is: " + verificationCode);
        driver.findElement(By.id("codeValue")).sendKeys(verificationCode);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        newEmail = new Email(emailAddress, password);
        newEmail.setFirstName(firstName);
        newEmail.setLastName(lastName);
        fillEmailProfile(newEmail);

        return newEmail;
    }

    private void browserTypeKeys(Actions builder, String input, int wordsPerMinute) {
        Actions typeAction;
        for(int count = 0; count < input.length(); count++) {
            typeAction = builder.sendKeys(Character.toString(input.charAt(count)));
            typeAction.perform();
            try {
                Thread.sleep((int) (1000 / ((double) wordsPerMinute / 60)) / 5); // divide by 5 to account for average word length
            } 
            catch (InterruptedException e) {
                System.out.println("Error when trying to pause keystrokes.");
            }
        }
    }

    private void fillEmailProfile(Email newEmail) {
        Random randomGen = new Random();

        newEmail.setGender(randomGen.nextInt(2) + 1);
        newEmail.setDateOfBirth(new GregorianCalendar(randomGen.nextInt(20) + 1970, randomGen.nextInt(12) + 1, randomGen.nextInt(28) + 1));
    }
}