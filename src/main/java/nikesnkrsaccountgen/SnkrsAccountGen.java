package nikesnkrsaccountgen;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SnkrsAccountGen extends AccountGenerator{
    Email accountInfo;
    WebDriver driver;

    public SnkrsAccountGen(Email accountInfo) {
        this.accountInfo = accountInfo;
        driver = new FirefoxDriver();
    }

    public void generateAccount() {
        WebDriverWait waiter = new WebDriverWait(driver, 20);
        String phoneNumber = getPhoneNumber(uk, pid)

        do {
            driver.get("https://nike.com");
            waiter.until(ExpectedConditions.elementToBeClickable(By.id("AccountNavigationContainer")));
            driver.findElement(By.id("AccountNavigationContainer")).click();
            driver.findElement(By.linkText("Join now.")).click();
            if(driver.findElements(By.id("nike-unite-date-id-yyyy")).size() == 0) {
                driver.quit();
                driver = new FirefoxDriver();
            }
        } while (driver.findElements(By.id("nike-unite-date-id-yyyy")).size() == 0);

        fillSignupForm();
        driver.findElement(By.xpath("//input[@value = 'CREATE ACCOUNT']")).click();
    }

    public String[] getAccountInfo() {
        String[] accountDetails = new String[]{accountInfo.getEmailAddress(), accountInfo.getPassword()};
        return accountDetails;
    } 

    private void fillSignupForm() {
        typeKeys(accountInfo.getEmailAddress(), 75, driver.findElement(By.name("emailAddress")));
        typeKeys(accountInfo.getPassword(), 75, driver.findElement(By.name("password")));
        typeKeys(accountInfo.getFirstName(), 75, driver.findElement(By.name("firstName")));
        typeKeys(accountInfo.getLastName(), 75, driver.findElement(By.name("lastName")));

        Select birthMonth = new Select(driver.findElement(By.id("nike-unite-date-id-mm")));
        birthMonth.selectByValue(accountInfo.getBirthMonth());
        Select birthDay = new Select(driver.findElement(By.id("nike-unite-date-id-dd")));
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

        /*
        if(accountInfo.getGender() == 1) {
            driver.findElement(By.xpath("//button[contains(text(),'Male')]")).click();
        }
        else {
            driver.findElement(By.xpath("//button[contains(text(),'Female')]")).click();
        }
        */
        try {
            driver.findElement(By.name("receiveEmail")).click();
        } catch (ElementNotInteractableException e) {
            System.out.println("Can't interact with element, ignorable");
        }

    }
}