public class Room {
    int roomNumber;
    String type;
    int capacity;
    String status;
    int guest ;
    int fee;

    public Room(RoomBuilder builder) {
        this.roomNumber = builder.roomNumber;
        this.type = builder.type;
        this.capacity = type.equals("Single") ? 1 : 2;
        this.status = builder.guest == 0 ? "Available" : "Not Available";
        this.guest = builder.guest;
        this.fee = type.equals("Single") ? 20 : 40;
    }

    public static class RoomBuilder {
        int roomNumber;
        String type;
        int capacity;
        String status;
        int guest ;
        int fee;

        public RoomBuilder(int roomNumber, String type) {
            this.roomNumber = roomNumber;
            this.type = type;
            this.capacity = type.equals("Single") ? 1 : 2;
            this.status = "Available";
            this.fee = type.equals("Single") ? 20 : 40;
        }

        public RoomBuilder assignGuest(int guest) {
            this.guest = guest;
            this.status = "Not Available";
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }

    @Override
    public String toString() {
        if (this.status.equals("Available")){
            return (roomNumber + "." + " " + type + " [" + status + "]");
        }
        else
            return (roomNumber + "." + " " + type + " [" + status + "]" + " " + "[Guest ID: " + guest + "] ");
    }
}
