public class Bill {
    int billNumber;
    String firstName;
    String lastName;
    int totalPrice;

    public Bill(String firstName, String lastName, int totalPrice) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return (firstName + " " + lastName + "$" + totalPrice + ".00");
    }
}
