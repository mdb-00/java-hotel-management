import java.util.ArrayList;
import java.util.List;

public class Guest {
    int guestID;
    String firstName;
    String lastName;
    int partySize;
    int daysStaying;
    List<Room> rooms = new ArrayList<>();

    public Guest(int guestID, String firstName, String lastName, int partySize, int daysStaying) {
        this.guestID = guestID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.partySize = partySize;
        this.daysStaying = daysStaying;
    }

    @Override
    public String toString() {
        return (guestID + "." + " " + firstName + " " + lastName + " " + "[Party Size: " + partySize +  "] " + "[Days Staying: " + daysStaying + "] ");
    }
}
