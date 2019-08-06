package nikesnkrsaccountgen;

public class App {
    public static void main( String[] args ) {
        System.setProperty("webdriver.gecko.driver","geckodriver.exe");
        GmailGen gen = new GmailGen();
        gen.getPhoneNumber();
    }
}
