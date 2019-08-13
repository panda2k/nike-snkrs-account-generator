package nikesnkrsaccountgen;

import java.util.Date;
import java.util.GregorianCalendar;

public class Gmail {
    String emailAddress;
    String password;
    String firstName;
    String lastName;
    String gender;
    GregorianCalendar dateOfBirth;

    public Gmail(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }    

    public void setFirstName(String firstName) {
        this.firstName = lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(GregorianCalendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender; 
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}