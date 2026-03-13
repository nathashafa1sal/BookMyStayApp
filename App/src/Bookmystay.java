
 //author Nathasha
 //version 2.0

public class Bookmystay {

    // Abstract Room class
    abstract static class Room {
        protected String type;
        protected int beds;
        protected double size; // in square meters
        protected double price; // per night

        public Room(String type, int beds, double size, double price) {
            this.type = type;
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        // Display room details
        public void displayDetails(int availability) {
            System.out.println("Room Type: " + type);
            System.out.println("Beds: " + beds);
            System.out.println("Size: " + size + " sqm");
            System.out.println("Price per night: $" + price);
            System.out.println("Available Rooms: " + availability);
            System.out.println("-----------------------------");
        }
    }

    // Concrete room types
    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 20.0, 75.0);
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 30.0, 120.0);
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 50.0, 250.0);
        }
    }

    // Static availability variables
    private static int singleRoomAvailability = 10;
    private static int doubleRoomAvailability = 5;
    private static int suiteRoomAvailability = 2;

    // Entry point
    public static void main(String[] args) {

        System.out.println("=== Welcome to Book My Stay – Room Availability ===\n");

        // Initialize room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Display room details and availability
        single.displayDetails(singleRoomAvailability);
        doubleRoom.displayDetails(doubleRoomAvailability);
        suite.displayDetails(suiteRoomAvailability);

        System.out.println("Thank you for using Book My Stay App!");
    }
}