
 //author Nathasha
 //version 3.0
 import java.util.HashMap;
 import java.util.Map;
public class Bookmystay{

// Abstract Room class (from Use Case 2)
     abstract static class Room {
         protected String type;
         protected int beds;
         protected double size;
         protected double price;

         public Room(String type, int beds, double size, double price) {
             this.type = type;
             this.beds = beds;
             this.size = size;
             this.price = price;
         }

         public void displayDetails(int availability) {
             System.out.println("Room Type: " + type);
             System.out.println("Beds: " + beds);
             System.out.println("Size: " + size + " sqm");
             System.out.println("Price per night: $" + price);
             System.out.println("Available Rooms: " + availability);
             System.out.println("-----------------------------");
         }
     }

     static class SingleRoom extends Room {
         public SingleRoom() { super("Single Room", 1, 20.0, 75.0); }
     }

     static class DoubleRoom extends Room {
         public DoubleRoom() { super("Double Room", 2, 30.0, 120.0); }
     }

     static class SuiteRoom extends Room {
         public SuiteRoom() { super("Suite Room", 3, 50.0, 250.0); }
     }

     /**
      * Centralized Room Inventory
      */
     static class RoomInventory {
         private Map<String, Integer> inventory;

         public RoomInventory() {
             inventory = new HashMap<>();
         }

         // Register a room type with initial availability
         public void addRoomType(String roomType, int count) {
             inventory.put(roomType, count);
         }

         // Retrieve availability
         public int getAvailability(String roomType) {
             return inventory.getOrDefault(roomType, 0);
         }

         // Update availability (increment or decrement)
         public boolean updateAvailability(String roomType, int change) {
             if (!inventory.containsKey(roomType)) return false;
             int current = inventory.get(roomType);
             int updated = current + change;
             if (updated < 0) return false; // prevent negative availability
             inventory.put(roomType, updated);
             return true;
         }

         // Display all room availability
         public void displayInventory() {
             System.out.println("=== Current Room Inventory ===");
             for (String type : inventory.keySet()) {
                 System.out.println(type + ": " + inventory.get(type) + " rooms available");
             }
             System.out.println("-----------------------------");
         }
     }

     // Entry point
     public static void main(String[] args) {

         System.out.println("=== Welcome to Book My Stay – Centralized Inventory ===\n");

         // Initialize room objects
         Room single = new SingleRoom();
         Room doubleRoom = new DoubleRoom();
         Room suite = new SuiteRoom();

         // Initialize centralized inventory
         RoomInventory inventory = new RoomInventory();
         inventory.addRoomType(single.type, 10);
         inventory.addRoomType(doubleRoom.type, 5);
         inventory.addRoomType(suite.type, 2);

         // Display rooms using polymorphism
         single.displayDetails(inventory.getAvailability(single.type));
         doubleRoom.displayDetails(inventory.getAvailability(doubleRoom.type));
         suite.displayDetails(inventory.getAvailability(suite.type));

         // Perform controlled updates
         inventory.updateAvailability("Single Room", -2); // 2 booked
         inventory.updateAvailability("Suite Room", 1);   // 1 added

         // Show updated inventory
         inventory.displayInventory();

         System.out.println("Thank you for using Book My Stay App!");
     }
 }