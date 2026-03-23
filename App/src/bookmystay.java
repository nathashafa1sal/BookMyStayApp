//author Nathasha
//version 5.0
import java.util.*;

// Reservation: represents booking intent
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        if (reservation == null) {
            System.out.println("Invalid reservation request.");
            return;
        }
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all queued requests (read-only)
    public void displayQueue() {
        if (queue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Request Queue (FIFO Order):");
        System.out.println("-----------------------------------");

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (without removing)
    public Reservation peekNext() {
        return queue.peek();
    }

    // For next use case (processing stage)
    public Reservation getNextRequest() {
        return queue.poll(); // removes in FIFO order
    }
}

// Main class
public class bookmystay {

    public static void main(String[] args) {

        // Step 1: Create Booking Queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Step 2: Simulate Guest Requests (arrival order matters)
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Suite");
        Reservation r3 = new Reservation("Charlie", "Double");

        // Step 3: Add requests to queue (FIFO)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Step 4: Display queue (preserves order)
        requestQueue.displayQueue();

        // Step 5: Show next request (without removing)
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.peekNext();
        if (next != null) {
            next.display();
        }
    }
}