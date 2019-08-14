package nikesnkrsaccountgen;

import java.util.GregorianCalendar;

public class Gmail {
    String emailAddress;
    String password;
    String firstName;
    String lastName;
    int gender; // 1 is male, 2 is female
    GregorianCalendar dateOfBirth;

    public Gmail(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(GregorianCalendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public GregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGender() {
        return gender; 
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}