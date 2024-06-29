import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManagement {
    public Connection connect_to_db(String dbName, String username, String password) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, username, password);
            if (conn != null) {
                System.out.println("Connection Established");
            }
            else
                System.out.println("Connection Failed");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    public void createGuest(Connection conn, Guest guest) {
        PreparedStatement preparedStatement;
        try {
            String query = "INSERT INTO guests (guestId, firstName, lastName, partySize, daysStaying) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, guest.guestID);
            preparedStatement.setString(2, guest.firstName);
            preparedStatement.setString(3, guest.lastName);
            preparedStatement.setInt(4, guest.partySize);
            preparedStatement.setInt(5, guest.daysStaying);
            preparedStatement.executeUpdate();
            System.out.println("Guest Created");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createRoom(Connection conn, Room room) {
        PreparedStatement preparedStatement;
        try {
            String query = "INSERT INTO rooms (roomNumber, type, capacity, status, fee) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, room.roomNumber);
            preparedStatement.setString(2, room.type);
            preparedStatement.setInt(3, room.capacity);
            preparedStatement.setString(4, room.status);
            preparedStatement.setInt(5, room.fee);
            preparedStatement.executeUpdate();
            System.out.println("Room Created");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public  void createBill(Connection conn) {
        PreparedStatement preparedStatement;
        try {
            String query = "INSERT INTO bills (firstName, lastName, totalPrice) SELECT g.firstName, g.lastName, g.daysStaying * r.fee as totalPrice FROM guests AS g INNER JOIN rooms AS r ON g.guestId = r.guestId";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.execute();
            System.out.println("Bill Created");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Guest> readAllGuests(Connection conn) {
        List<Guest> guests = new ArrayList<>();
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM guests";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                int guestId = rs.getInt("guestId");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int partySize = rs.getInt("partySize");
                int daysStaying = rs.getInt("daysStaying");
                Guest guest = new Guest(guestId, firstName, lastName, partySize, daysStaying);
                guests.add(guest);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return guests;
    }

    public List<Room> readAllRooms(Connection conn) {
        List<Room> rooms = new ArrayList<>();
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM rooms";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                int roomNumber = rs.getInt("roomNumber");
                String type = rs.getString("type");
                int guest = rs.getInt("guestId");
                Room.RoomBuilder builder = new Room.RoomBuilder(roomNumber, type);
                builder.assignGuest(guest);
                Room room = new Room(builder);
                rooms.add(room);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return rooms;
    }

    public List<Bill> readAllBills(Connection conn) {
        List<Bill> bills = new ArrayList<>();
        Statement statement;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM bills";
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int totalPrice = rs.getInt("totalPrice");
                Bill bill = new Bill(firstName, lastName, totalPrice);
                bills.add(bill);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return bills;
    }

    public void updateFirstName(Connection conn, int guestId, String newFirstName) {
        Statement statement;
        try {
            String query = String.format("UPDATE guests SET firstName = '%s' WHERE guestId = '%x'", newFirstName, guestId);
            statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateLastName(Connection conn, int guestId, String newLastName) {
        PreparedStatement preparedStatement;
        try {
            String query = String.format("UPDATE guests SET lastName = '%s' WHERE guestId = '%x'", newLastName, guestId);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updatePartySize(Connection conn, int guestId, int newPartySize) {
        PreparedStatement preparedStatement;
        try {
            String query = String.format("UPDATE guests SET partySize = '%x' WHERE guestId = '%x'", newPartySize, guestId);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateDaysStaying(Connection conn, int guestId, int newDaysStaying) {
        PreparedStatement preparedStatement;
        try {
            String query = String.format("UPDATE guests SET daysStaying = '%x' WHERE guestId = '%x'", newDaysStaying, guestId);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void assignGuest(Connection conn, int guestId, int roomNumber) {
        Statement statement;
        try {
            String query = String.format("UPDATE rooms SET guestId = '%x', status = 'Not Available' WHERE roomNumber = '%x' ", guestId, roomNumber);
            statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void checkOutGuest(Connection conn, int roomNumber) {
        Statement statement;
        try {
            String query = String.format("UPDATE rooms SET guestId = null, status = 'Available' WHERE roomNumber = '%x' ", roomNumber);
            statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void searchByName(Connection conn, String tableName, String firstName) {
        PreparedStatement preparedStatement;
        ResultSet rs;
        try {
            String query = String.format("SELECT * FROM %s WHERE firstName = '%s'", tableName, firstName);
            preparedStatement = conn.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("guestId"));
                System.out.println(rs.getString("firstName"));
                System.out.println(rs.getString("lastName"));
                System.out.println(rs.getString("partySize"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteGuest(Connection conn, int guestId) {
        PreparedStatement preparedStatement;
        try {
            String query = String.format("DELETE FROM guests WHERE guestId = %x", guestId);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.executeQuery();
            System.out.println("Guest Deleted");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
