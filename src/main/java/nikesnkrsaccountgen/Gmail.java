package nikesnkrsaccountgen;

import java.util.Date;

public class Gmail {
    String emailAddress;
    String password;
    String firstName;
    String lastName;
    String gender;
    Date dateOfBirth;

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

    public void setDateOfBirth(int year, int month, int day) {
        dateOfBirth = new Date(year, month, day); // deprecated but its ok for this purpose
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