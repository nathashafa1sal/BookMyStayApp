//author Nathasha
//version 10.0
import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return isCancelled; }

    public void cancel() {
        this.isCancelled = true;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Status: " + (isCancelled ? "CANCELLED" : "CONFIRMED"));
    }
}

class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public void increment(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        System.out.println("\nBooking History:");
        for (Reservation r : reservations.values()) {
            r.display();
        }
    }
}

class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        Reservation r = history.getReservation(reservationId);

        // Step 1: Validation
        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation does not exist.");
            return;
        }

        if (r.isCancelled()) {
            System.out.println("Cancellation FAILED: Already cancelled.");
            return;
        }

        // Step 2: Record rollback (LIFO)
        rollbackStack.push(r.getRoomId());

        // Step 3: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 4: Update booking status
        r.cancel();

        // Step 5: Confirmation
        System.out.println("Cancellation SUCCESSFUL");
        System.out.println("Released Room ID: " + r.getRoomId());
        System.out.println("Updated Availability for " + r.getRoomType() +
                ": " + inventory.getAvailability(r.getRoomType()));
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Most Recent First): " + rollbackStack);
    }
}

public class bookmystay {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 0); // already booked
        inventory.addRoomType("Suite", 0);

        // Step 2: Booking History (simulating confirmed bookings)
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("R1", "Alice", "Single", "SI-111");
        Reservation r2 = new Reservation("R2", "Bob", "Suite", "SU-222");

        history.addReservation(r1);
        history.addReservation(r2);

        // Step 3: Cancellation Service
        CancellationService cancelService = new CancellationService(inventory, history);

        // Step 4: Perform cancellations
        cancelService.cancelBooking("R1");  // valid
        cancelService.cancelBooking("R1");  // duplicate
        cancelService.cancelBooking("R3");  // invalid

        // Step 5: Show rollback stack
        cancelService.showRollbackStack();

        // Step 6: Final booking history
        history.displayAll();
    }
}