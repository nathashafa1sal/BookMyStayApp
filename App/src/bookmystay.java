//author Nathasha
//version 8.0
import java.util.*;

// Reservation (enhanced with roomId for confirmed bookings)
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void display() {
        System.out.println("Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// Booking History (State Holder)
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            history.add(reservation); // preserves insertion order
        }
    }

    // Read-only access
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

// Reporting Service (Separation of Concerns)
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> reservations) {

        if (reservations.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }

        System.out.println("\nAll Confirmed Bookings:");
        System.out.println("------------------------");

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Summary report
    public void generateSummary(List<Reservation> reservations) {

        System.out.println("\nBooking Summary रिपोर्ट:");
        System.out.println("------------------------");

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomTypeCount.get(type));
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// Main Class
public class bookmystay {

    public static void main(String[] args) {

        // Step 1: Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("Alice", "Single", "SI-111AAA"));
        history.addReservation(new Reservation("Bob", "Suite", "SU-222BBB"));
        history.addReservation(new Reservation("Charlie", "Single", "SI-333CCC"));

        // Step 3: Reporting Service
        BookingReportService reportService = new BookingReportService();

        // Step 4: Admin views all bookings
        reportService.showAllBookings(history.getAllReservations());

        // Step 5: Admin generates summary report
        reportService.generateSummary(history.getAllReservations());
    }
}