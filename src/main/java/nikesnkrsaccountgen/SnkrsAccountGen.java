package nikesnkrsaccountgen;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SnkrsAccountGen extends AccountGenerator{
    WebDriver driver;
    private final int typingSpeed = 100;

    public SnkrsAccountGen(WebDriver driver) {
        this.driver = driver;
    }

    public Email generateAccount(String countryCode, Email accountInfo) {
        WebDriverWait waiter = new WebDriverWait(driver, 20);

        do {
            driver.get("https://www.nike.com/login");
            waiter.until(ExpectedConditions.elementToBeClickable(By.linkText("Join now.")));
            driver.findElement(By.linkText("Join now.")).click();
            if(driver.findElements(By.id("nike-unite-date-id-yyyy")).size() == 0) {
                WebDriver driver2 = new FirefoxDriver();
                Email createdAccount = new SnkrsAccountGen(driver2).generateAccount(countryCode, accountInfo);
                driver2.quit();
                return createdAccount;
            }
        } while (driver.findElements(By.id("nike-unite-date-id-yyyy")).size() == 0);

        fillSignupForm(accountInfo);
        driver.findElement(By.xpath("//input[@value = 'CREATE ACCOUNT']")).click();

        verifyPhoneNumber(countryCode);
        
        return accountInfo;
    }

    private void verifyPhoneNumber(String countryCode) {
        WebDriverWait waiter = new WebDriverWait(driver, 20);

        String verificationCode = "";
        String verificationMessage;


        if(countryCode.equals("cn")) {
            String phoneNumber = getPhoneNumber(countryCode, "628");
            System.out.println("Got phone number: " + phoneNumber);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Caught exception while waiting for setting refresh");
            }
            driver.get("https://www.nike.com/cn/member/settings");
            waiter.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'添加')]")));
            driver.findElement(By.xpath("//button[contains(text(),'添加')]")).click();
            typeKeys(phoneNumber.substring(3), typingSpeed, driver.findElement(By.xpath("//input[@data-componentname='phoneNumber']")));
            driver.findElement(By.className("sendCodeButton")).click();

            verificationMessage = getMessage(phoneNumber.substring(1), countryCode, "628");
            if(verificationMessage.equals("no message recieved")) {
                System.out.println("No message recieved. Retrying");
                verifyPhoneNumber(countryCode);
                return;
            }
            else {
                for(int count = 1; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
                    if(Character.isDigit(verificationMessage.charAt(count))) {
                        verificationCode += verificationMessage.charAt(count);
                    }
                }
                System.out.println("Verification code is: " + verificationCode);
            }

            typeKeys(verificationCode, typingSpeed, driver.findElement(By.xpath("//input[@placeholder='输入验证码']")));
            driver.findElement(By.xpath("//input[@value='继续']")).click();
            blacklistNumber(phoneNumber, countryCode, "628");
        }
        else {
            String phoneNumber = getPhoneNumber(countryCode, "462");
            System.out.println("Got phone number: " + phoneNumber);
            
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Caught exception while waiting for setting refresh");
            }
            
            driver.get("https://www.nike.com/member/settings");
            driver.findElement(By.xpath("//button[@aria-label='Add Mobile Number']")).click();
            Select phoneNumberRegion = new Select(driver.findElement(By.className("country")));
            try {
                phoneNumberRegion.selectByValue("GB");
            } catch (ElementClickInterceptedException e) {
                System.out.println("Caught exception when selecting country. Retrying");
                try {
                    Thread.sleep(2500);
                    phoneNumberRegion.selectByValue("GB");
                } catch (InterruptedException f) {
                    System.out.println("Caught error while sleeping.");
                }
            }
            typeKeys(phoneNumber.substring(3), typingSpeed, driver.findElement(By.xpath("//input[@placeholder='Mobile Number']")));
            driver.findElement(By.id("progressiveMobile")).click();
            driver.findElement(By.className("sendCodeButton")).click();
            verificationMessage = getMessage(phoneNumber, countryCode, "462");

            if(verificationMessage.equals("no message recieved")) {
                System.out.println("No message recieved. Retrying");
                verifyPhoneNumber(countryCode);
            }
            else {
                for(int count = 1; count < verificationMessage.length(); count++) { // start count at 2 to get out of starting message
                    if(Character.isDigit(verificationMessage.charAt(count))) {
                        verificationCode += verificationMessage.charAt(count);
                    }
                }
                System.out.println("Verification code is: " + verificationCode);
            }

            typeKeys(verificationCode, typingSpeed, driver.findElement(By.xpath("//input[@placeholder='Enter Code']")));
            try {
                driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
            } catch (NoSuchElementException e) {
                System.out.println("Caught exception while clicking continue. Account still should be verified");
            }

            blacklistNumber(phoneNumber, countryCode, "462");
        }
        
    }

    private void fillSignupForm(Email accountInfo) {
        typeKeys(accountInfo.getEmailAddress(), typingSpeed, driver.findElement(By.name("emailAddress")));
        typeKeys(accountInfo.getPassword(), typingSpeed, driver.findElement(By.name("password")));
        typeKeys(accountInfo.getFirstName(), typingSpeed, driver.findElement(By.name("firstName")));
        typeKeys(accountInfo.getLastName(), typingSpeed, driver.findElement(By.name("lastName")));

        Select birthMonth = new Select(driver.findElement(By.id("nike-unite-date-id-mm")));
        birthMonth.selectByValue(accountInfo.getBirthMonth());
        Select birthDay = new Select(driver.findElement(By.id("nike-unite-date-id-dd")));
        System.out.println("birth day: " + accountInfo.getBirthDay());
        birthDay.selectByValue(accountInfo.getBirthDay());
        Select birthYear = new Select(driver.findElement(By.id("nike-unite-date-id-yyyy")));
        birthYear.selectByValue(accountInfo.getBirthYear());
        Select country = new Select(driver.findElement(By.name("country")));
        country.selectByValue("GB");

        if(accountInfo.getGender() == 1) {
            driver.findElement(By.xpath("//span[contains(text(),'Male')]")).click();
        }
        else {
            driver.findElement(By.xpath("//span[contains(text(),'Female')]")).click();
        }
        
        try {
            driver.findElement(By.name("receiveEmail")).click();
        } catch (ElementNotInteractableException e) {
            System.out.println("Can't interact with element, ignorable");
        }
        
    }
}