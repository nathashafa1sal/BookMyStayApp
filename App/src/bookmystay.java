//author Nathasha
//version 9.0
import java.util.*;


// Base exception
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Specific exceptions
class InvalidRoomTypeException extends BookingException {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class NoAvailabilityException extends BookingException {
    public NoAvailabilityException(String message) {
        super(message);
    }
}

class InvalidGuestException extends BookingException {
    public InvalidGuestException(String message) {
        super(message);
    }
}


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


class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) throws BookingException {
        int current = getAvailability(type);

        if (current <= 0) {
            throw new NoAvailabilityException("No rooms available for type: " + type);
        }

        availability.put(type, current - 1);
    }
}


class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws BookingException {

        // Guest validation
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidGuestException("Guest name cannot be empty.");
        }

        // Room type validation
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidRoomTypeException(
                    "Invalid room type: " + reservation.getRoomType()
            );
        }

        // Availability validation (fail-fast)
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new NoAvailabilityException(
                    "No availability for room type: " + reservation.getRoomType()
            );
        }
    }
}


class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {

        try {
            // Step 1: Validate (Fail Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room (simplified)
            String roomId = generateRoomId(reservation.getRoomType());

            // Step 3: Update inventory safely
            inventory.decrement(reservation.getRoomType());

            // Step 4: Success message
            System.out.println("Booking CONFIRMED");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType());
            System.out.println("Room ID: " + roomId);
            System.out.println("---------------------------");

        } catch (BookingException e) {
            // Graceful failure handling
            System.out.println("Booking FAILED: " + e.getMessage());
            System.out.println("---------------------------");
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + "-" +
                UUID.randomUUID().toString().substring(0, 5);
    }
}


public class bookmystay{

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Suite", 0);

        BookingService bookingService = new BookingService(inventory);


        bookingService.confirmBooking(new Reservation("Alice", "Single"));

        bookingService.confirmBooking(new Reservation("Bob", "Deluxe"));

        bookingService.confirmBooking(new Reservation("Charlie", "Suite"));

        bookingService.confirmBooking(new Reservation("", "Single"));

        bookingService.confirmBooking(new Reservation("David", "Single"));
    }
}