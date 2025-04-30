public class Passenger {
    String fullName;
    String passportNumber;

    public Passenger(String fullName, String passportNumber) {
        this.fullName = fullName;
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        return fullName + " (Passport: " + passportNumber + ")";
    }
}