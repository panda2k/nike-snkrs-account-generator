package nikesnkrsaccountgen;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Email {
    String emailAddress;
    String password;
    String firstName;
    String lastName;
    int gender; // 1 is male, 2 is female
    GregorianCalendar dateOfBirth;

    public Email(String emailAddress, String password) {
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

    public String getBirthMonth() {
        if(dateOfBirth.get(Calendar.MONTH) < 10) {
            return "0" + dateOfBirth.get(Calendar.MONTH);
        }
        else {
            return Integer.toString(dateOfBirth.get(Calendar.MONTH));
        }
    }

    public String getBirthYear() {
        return Integer.toString(dateOfBirth.get(Calendar.YEAR));
    }

    public String getBirthDay() {
        if(dateOfBirth.get(Calendar.MONTH) < 10) {
            return "0" + dateOfBirth.get(Calendar.DAY_OF_MONTH);
        }
        else {
            return Integer.toString(dateOfBirth.get(Calendar.MONTH));
        }
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