import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DbManagement db = new DbManagement();
        Connection conn = db.connect_to_db("hotel-management", "postgres", "12345");
        Scanner scanner = new Scanner(System.in);
        Boolean running = true;

        while (running) {
            System.out.println("[C]reate, [V]iew, [M]anage entries, or [Q]uit");
            System.out.print("> ");
            String action = scanner.nextLine().toUpperCase();

            switch (action) {
                case "C":
                    System.out.println("Create [G]uest or [R]oom");
                    String choice = scanner.nextLine().toUpperCase();
                    if (choice.equals("G")) {
                        System.out.print("ID: ");
                        int guestId = scanner.nextInt();
                        System.out.print("First Name: ");
                        String firstName = scanner.nextLine().toUpperCase();
                        System.out.print("Last Name: ");
                        String lastName = scanner.nextLine().toUpperCase();
                        System.out.print("Party Size: ");
                        int partySize = scanner.nextInt();
                        System.out.print("Days Staying: ");
                        int daysStaying = scanner.nextInt();
                        Guest guest = new Guest(guestId, firstName, lastName, partySize, daysStaying);
                        db.createGuest(conn, guest);
                    }
                    else if (choice.equals("R")) {
                        System.out.println("Room NUmber: ");
                        int roomNumber = scanner.nextInt();
                        System.out.println("[S]ingle or [D]ouble room");
                        String roomChoice = scanner.nextLine().toUpperCase();
                        String roomType = roomChoice.equals("S") ? "Single" : "Double";
                        Room.RoomBuilder builder = new Room.RoomBuilder(roomNumber, roomType);
                        Room room = new Room(builder);
                        db.createRoom(conn, room);
                    }
                    break;
                case "V":
                    System.out.println("View [G]uests, [R]ooms, or [B]ills");
                    String viewChoice = scanner.nextLine().toUpperCase();
                    if (viewChoice.equals("G")) {
                        List<Guest> guests = db.readAllGuests(conn);
                        for (Guest guest : guests) {
                            System.out.println(guest);
                        }
                    }
                    else if (viewChoice.equals("R")) {
                        List<Room> rooms = db.readAllRooms(conn);
                        for (Room room : rooms) {
                            System.out.println(room);
                        }
                    }
                    else if (viewChoice.equals("B")) {
                        List<Bill> bills = db.readAllBills(conn);
                        for (Bill bill : bills) {
                            System.out.println(bill);
                        }
                    }
                    break;
                case "M":
                    System.out.println("[U]pdate, [D]elete, [A]ssign, or [C]heckout guests");
                    choice = scanner.nextLine().toUpperCase();
                    switch (choice) {
                        case "U":
                            System.out.println("Enter the guest ID to update: ");
                            int guestIdUpdate = scanner.nextInt();
                            System.out.println("1. First Name");
                            System.out.println("2. Last Name");
                            System.out.println("3. Party Size");
                            System.out.println("4. Days Staying");
                            int updateGuestChoice = scanner.nextInt();
                            switch (updateGuestChoice) {
                                case 1:
                                    System.out.print("New First Name: ");
                                    scanner.nextLine();
                                    String newFirstName = scanner.nextLine().toUpperCase();
                                    db.updateFirstName(conn, guestIdUpdate, newFirstName);
                                    break;
                                case 2:
                                    System.out.print("New Last Name: ");
                                    scanner.nextLine();
                                    String newLastName = scanner.nextLine().toUpperCase();
                                    db.updateLastName(conn, guestIdUpdate, newLastName);
                                    break;
                                case 3:
                                    System.out.print("New Party Size: ");
                                    scanner.nextLine();
                                    int newPartySize = scanner.nextInt();
                                    db.updatePartySize(conn, guestIdUpdate, newPartySize);
                                    break;
                                case 4:
                                    System.out.println("New Days Staying: ");
                                    scanner.nextLine();
                                    int newDaysStaying = scanner.nextInt();
                                    db.updateDaysStaying(conn, guestIdUpdate, newDaysStaying);
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;
                        case "D":
                            System.out.println("Enter the guest ID to delete: ");
                            int guestIdDelete = scanner.nextInt();
                            db.deleteGuest(conn, guestIdDelete);
                            break;
                        case "A":
                            System.out.println("Room Number: ");
                            int availableRoomNumber = scanner.nextInt();
                            System.out.println("Guest ID: ");
                            int guestId = scanner.nextInt();
                            db.assignGuest(conn, guestId, availableRoomNumber);
                            db.createBill(conn);
                            break;
                        case "C":
                            System.out.println("Room Number: ");
                            int unavailableRoomNumber = scanner.nextInt();
                            db.checkOutGuest(conn, unavailableRoomNumber);
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    break;
                    }
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}