package nikesnkrsaccountgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebElement;

public class AccountGenerator {
    final String username = "";
    final String token = "";

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

    public void typeKeys(String input, int wordsPerMinute, WebElement element) {
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

    public String randomPassword(int characterCount) {
        String password = "";
        Random randomGen = new Random();

        for(int count = 0; count < characterCount; count++) {
            password += (char) (randomGen.nextInt(75) + 48);
            if(password.charAt(count) == ';') {
                password = password.substring(0, count + 1);
                count--;
            }
        }

        password += (char) (randomGen.nextInt(27) + 98); // make sure password has a lowercase letter
        password += (char) (randomGen.nextInt(27) + 65); // make sure password has an uppercase letter
        password += (char) (randomGen.nextInt(15) + 33); // make sure password has a symbol 
        password += (char) (randomGen.nextInt(10) + 48); // make sure password has a number

        return password;
    }
    public String getPhoneNumber(String countryCode, String pid) {
        String getNumberURL;
        if(countryCode.equals("cn")) {
            getNumberURL = "http://api.getsmscode.com/do.php" + "?action=getmobile" + "&username=" + username + "&token=" + token + "&pid=" + pid;
        }
        else {
            getNumberURL = "http://api.getsmscode.com/vndo.php" + "?action=getmobile" + "&username=" + username + "&token=" + token + "&pid=" + pid + "&cocode=" + countryCode; 
        }
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
        
        if(phoneNumber.equals("+Message|unavailable")) {
            System.out.println("No phone numbers available, waiting a minute");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                System.out.println("Caught excception while waiting for new phone number");
            }
            getPhoneNumber(countryCode, pid);
        }

        return phoneNumber;
    }

    public String getMessage(String phoneNumber, String countryCode, String pid) {
        int refreshCount = 0;
        String getMessageURL; 
        String defaultMessage;
        if(countryCode.equals("cn")) {
            defaultMessage = "Message|not receive";
        }
        else {
            defaultMessage = "Message|Not Receive";
        }
        String message = defaultMessage; 

        if(countryCode.equals("cn")) {
            getMessageURL = "http://api.getsmscode.com/do.php" + "?action=getsms" + "&username=" + username + "&token=" + token + "&pid=" + pid + "&mobile=" + phoneNumber;
        }
        else {
            getMessageURL = "http://api.getsmscode.com/vndo.php" + "?action=getsms" + "&username=" + username + "&token=" + token + "&pid=" + pid + "&mobile=" + phoneNumber + "&cocode=" + countryCode; 
        }
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
    
            if(message.equals(defaultMessage)) {
                try {
                    if(refreshCount >= 6) {
                        return "no message recieved";
                    }
                    System.out.println("No message recieved, sleeping for 10 seconds");
                    Thread.sleep(10000);
                    refreshCount++;
                } 
                catch (InterruptedException e) {
                    System.out.println("Unable to pause thread");
                }
            }
            
        } while(message.equals(defaultMessage));
        System.out.println("Receieved message\nMessage:'" + message + "'");
        return message;
    }

    public void blacklistNumber(String phoneNumber, String countryCode, String pid) {
        String blacklistNumberURL; 
        if(countryCode.equals("cn")) {
            blacklistNumberURL = "http://api.getsmscode.com/do.php" + "?action=addblack" + "&username=" + username + "&token=" + token + "&pid=" + pid + "&mobile=" + phoneNumber;
        }
        else {
            blacklistNumberURL = "http://api.getsmscode.com/vndo.php" + "?action=addblack" + "&username=" + username + "&token=" + token + "&pid=" + pid + "&mobile=" + phoneNumber + "&cocode=" + countryCode; 
        }
        HttpPost getSMSRequest = new HttpPost(blacklistNumberURL);
        getSMSRequest.addHeader("Accept", "application/json");
        getSMSRequest.addHeader("Content-type", "application/json");

        System.out.println("Now executing API request " + getSMSRequest.getRequestLine());
        try {
            String response = requestsClient.execute(getSMSRequest, errorHandler);
            System.out.println("response: " + response);
        } 
        catch (IOException e) {
            System.out.println("IOException. Please check URL parameters");
        }
    }

    public String randomNumberString(int digits) {
        Random randomGen = new Random();
        String randomNumbers = "";

        for(int count = 0; count < digits; count++) {
            randomNumbers += randomGen.nextInt(10);
        }

        return randomNumbers;
    }
    public String getFirstName() {
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
    public String getLastName() {
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

    public void fillEmailProfile(Email newEmail) {
        Random randomGen = new Random();

        newEmail.setGender((randomGen.nextInt(2) + 1));
        newEmail.setDateOfBirth(new GregorianCalendar((randomGen.nextInt(20) + 1970), (randomGen.nextInt(12) + 1), (randomGen.nextInt(28) + 1)));
    }
}