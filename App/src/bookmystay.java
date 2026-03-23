//author Nathasha
//version 6.0
import java.util.*;
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service (State + Update)
class InventoryService {
    private Map<String, Integer> availability;

    public InventoryService() {
        availability = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Core Logic)
class BookingService {

    private InventoryService inventory;

    // Track all allocated room IDs globally
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map roomType -> allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
    }

    public void processReservation(Reservation reservation) {

        if (reservation == null) return;

        String type = reservation.getRoomType();

        // Step 1: Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking FAILED for " + reservation.getGuestName() + " (No availability)");
            return;
        }

        // Step 2: Generate unique room ID
        String roomId;
        do {
            roomId = generateRoomId(type);
        } while (allocatedRoomIds.contains(roomId)); // enforce uniqueness

        // Step 3: Allocate (atomic logical unit)
        allocatedRoomIds.add(roomId);

        roomAllocations
                .computeIfAbsent(type, k -> new HashSet<>())
                .add(roomId);

        // Step 4: Update inventory immediately
        inventory.decrement(type);

        // Step 5: Confirm booking
        System.out.println("Booking CONFIRMED");
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Assigned Room ID: " + roomId);
        System.out.println("---------------------------");
    }

    // Utility to show allocations
    public void displayAllocations() {
        System.out.println("\nRoom Allocations Summary:");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
    }
}

// Main Class
public class bookmystay {

    public static void main(String[] args) {

        // Step 1: Inventory Setup
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Suite", 1);

        // Step 2: Booking Queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single")); // should fail
        queue.addRequest(new Reservation("David", "Suite"));

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Step 5: Show final allocations
        bookingService.displayAllocations();
    }
}