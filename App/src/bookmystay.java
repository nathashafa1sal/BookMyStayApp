//author : Nathasha
//version 12.0

import java.io.*;
import java.util.*;


class Room implements Serializable {
    private int roomId;
    private String type;
    private boolean isAvailable;

    public Room(int roomId, String type) {
        this.roomId = roomId;
        this.type = type;
        this.isAvailable = true;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void book() {
        isAvailable = false;
    }

    public void release() {
        isAvailable = true;
    }

    public String toString() {
        return "Room ID: " + roomId + ", Type: " + type + ", Available: " + isAvailable;
    }
}

class Booking implements Serializable {
    private int bookingId;
    private int roomId;
    private String customerName;

    public Booking(int bookingId, int roomId, String customerName) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String toString() {
        return "Booking ID: " + bookingId +
                ", Room ID: " + roomId +
                ", Customer: " + customerName;
    }
}

class HotelSystem implements Serializable {

    private Map<Integer, Room> rooms = new HashMap<>();
    private List<Booking> bookings = new ArrayList<>();
    private int bookingCounter = 1;

    public HotelSystem() {
        if (rooms.isEmpty()) {
            rooms.put(1, new Room(1, "Single"));
            rooms.put(2, new Room(2, "Double"));
            rooms.put(3, new Room(3, "Suite"));
        }
    }

    public void showRooms() {
        for (Room room : rooms.values()) {
            System.out.println(room);
        }
    }

    public void bookRoom(int roomId, String customerName) {
        Room room = rooms.get(roomId);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("Room already booked!");
            return;
        }

        room.book();
        Booking booking = new Booking(bookingCounter++, roomId, customerName);
        bookings.add(booking);

        System.out.println("Booking successful: " + booking);
    }

    public void cancelBooking(int bookingId) {
        Iterator<Booking> it = bookings.iterator();

        while (it.hasNext()) {
            Booking b = it.next();

            if (b.getBookingId() == bookingId) {
                rooms.get(b.getRoomId()).release();
                it.remove();
                System.out.println("Booking cancelled.");
                return;
            }
        }

        System.out.println("Booking not found.");
    }

    public void showBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Booking b : bookings) {
            System.out.println(b);
        }
    }
}

class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    public static void save(HotelSystem system) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(system);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static HotelSystem load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous data found. Starting fresh.");
            return new HotelSystem();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System restored successfully.");
            return (HotelSystem) in.readObject();

        } catch (Exception e) {
            System.out.println("Corrupted data detected. Starting fresh.");
            return new HotelSystem();
        }
    }
}


public class bookmystay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Load saved state
        HotelSystem system = PersistenceService.load();

        while (true) {
            System.out.println("\n===== HOTEL BOOKING SYSTEM =====");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    system.showRooms();
                    break;

                case 2:
                    System.out.print("Enter Room ID: ");
                    int roomId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Customer Name: ");
                    String name = sc.nextLine();

                    system.bookRoom(roomId, name);
                    break;

                case 3:
                    System.out.print("Enter Booking ID: ");
                    int bookingId = sc.nextInt();

                    system.cancelBooking(bookingId);
                    break;

                case 4:
                    system.showBookings();
                    break;

                case 5:
                    PersistenceService.save(system);
                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}