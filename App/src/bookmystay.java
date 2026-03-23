//author Nathasha
//version 11.0
import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// ---------------- THREAD-SAFE INVENTORY ----------------
class InventoryService {

    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2); // limited rooms
    }

    // synchronized method (critical section)
    public synchronized boolean allocateRoom(String roomType) {

        int available = availability.getOrDefault(roomType, 0);

        if (available <= 0) {
            return false;
        }

        // simulate delay (to expose race conditions if not synchronized)
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        availability.put(roomType, available - 1);
        return true;
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// ---------------- THREAD-SAFE QUEUE ----------------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized enqueue
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    // synchronized dequeue
    public synchronized Reservation getNext() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- BOOKING PROCESSOR (THREAD) ----------------
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // synchronized access to queue
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getNext();
            }

            if (r == null) continue;

            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " CONFIRMED booking for " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED booking for " + r.getGuestName() + " (No availability)");
            }
        }
    }
}

// ---------------- MAIN ----------------
public class bookmystay {

    public static void main(String[] args) {

        // Shared resources
        BookingQueue queue = new BookingQueue();
        InventoryService inventory = new InventoryService();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single"));

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(queue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        // Final state
        System.out.println("\nFinal Availability: " + inventory.getAvailability("Single"));
    }
}