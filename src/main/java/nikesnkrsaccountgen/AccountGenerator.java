package nikesnkrsaccountgen;

import java.io.IOException;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebElement;

public class AccountGenerator {
    final String getSMSCodeAPIURL = "http://api.getsmscode.com/vndo.php";
    final String username = "littlwang@gmail.com";
    final String token = "36d0674c163d6b68ae2ccc89e461ed1f";
    
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
            password += (char) randomGen.nextInt(75) + 48;
        }

        return password;
    }
    public String getPhoneNumber() {
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

    public String getMessage(String phoneNumber) {
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

    public void blacklistNumber(String phoneNumber) {
        String blacklistNumberURL = getSMSCodeAPIURL + "?action=addblack&username=" + username + "&token=" + token + "&pid=1&mobile=" + phoneNumber + "&cocode=uk"; 
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

}