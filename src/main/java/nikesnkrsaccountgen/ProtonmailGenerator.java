package nikesnkrsaccountgen;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
        String phoneNumber;

        driver.get("https://mail.protonmail.com/create/new?language=en");
        typeKeys(emailAddress, 75, driver.findElement(By.id("username")));
        typeKeys(password, 75, driver.findElement(By.id("password")));
        typeKeys(password, 75, driver.findElement(By.id("passwordc")));
        driver.findElement(By.name("submitBtn")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("confirmModalBtn")));
        driver.findElement(By.id("confirmModalBtn")).click();
        waiter.until(ExpectedConditions.elementToBeClickable(By.id("id-signup-radio-sms")));
        driver.findElement(By.id("id-signup-radio-sms")).click();

        return newEmail;
    }
}