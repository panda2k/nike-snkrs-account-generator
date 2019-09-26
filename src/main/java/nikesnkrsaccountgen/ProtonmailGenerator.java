package nikesnkrsaccountgen;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProtonmailGenerator extends AccountGenerator{
    WebDriver driver;

    public ProtonmailGenerator(WebDriver driver) {
        this.driver = driver;
    }

    public Email generateAccount() {
        System.out.println("Generating protonmail account");
        WebDriverWait waiter = new WebDriverWait(driver, 60);
        System.out.println("Initialized waiter object");
        Email newEmail;
        System.out.println("Initialized email object");
        String firstName = getFirstName();
        System.out.println("Retrieved first name");
        String lastName = getLastName();
        System.out.println("Retrieved last name");
        String emailAddress = firstName + lastName.charAt(0) + randomNumberString(10);
        System.out.println("Determined email address");
        String password = randomPassword(10);
        System.out.println("Generated password");
        String phoneNumber = getPhoneNumber("cn", "404");
        System.out.println("Retrieved phone number");
        Actions builder = new Actions(driver);
        System.out.println("Initialized Action builder");
        String verificationMessage;
        String verificationCode = "";
        System.out.println("Initialized verification strings");

        System.out.println("Initialized all objects and variables in proton mail generator");
        
        driver.get("https://mail.protonmail.com/create/new?language=en");
        System.out.println("Retrieved Signup Page");
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
            Thread.sleep(3500);
        } catch (Exception e) {
            System.out.println("Caught exception while sleeping");
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

        try {
            waiter.until(ExpectedConditions.elementToBeClickable(By.id("confirmModalBtn")));
        } catch (TimeoutException e) {
            System.out.println("Retrying protonmail signup");
            return generateAccount();
        }
        driver.findElement(By.id("confirmModalBtn")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='humanVerification-block-sms']//label[@class='signup-radio-label']"))).click();
        typeKeys(phoneNumber, 75, driver.findElement(By.id("smsVerification")));
        driver.findElement(By.xpath("//button[@class='pm_button primary codeVerificator-btn-send']")).click();

        verificationMessage = getMessage(phoneNumber.substring(1), "cn", "404");
        if(verificationMessage.equals("no message recieved")) {
            return generateAccount();
        }
        for(int count = 1; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
            if(Character.isDigit(verificationMessage.charAt(count))) {
                verificationCode += verificationMessage.charAt(count);
            }
        }

        System.out.println("code is: " + verificationCode);
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("codeValue")));
        driver.findElement(By.id("codeValue")).sendKeys(verificationCode);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        emailAddress += "@protonmail.com";

        newEmail = new Email(emailAddress, password);
        newEmail.setFirstName(firstName);
        newEmail.setLastName(lastName);
        fillEmailProfile(newEmail);

        waiter.until(ExpectedConditions.elementToBeClickable(By.id("displayName")));

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
}