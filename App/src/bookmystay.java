//author Nathasha
//version 6.0
import java.util.*;

// Add-On Service (independent business feature)
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {

        if (reservationId == null || services == null || services.isEmpty()) {
            System.out.println("Invalid service request.");
            return;
        }

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .addAll(services);

        System.out.println("Services added for Reservation ID: " + reservationId);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        double total = 0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("\nAdd-On Services for Reservation ID: " + reservationId);

        for (AddOnService s : services) {
            System.out.println("- " + s.getName() + " (₹" + s.getCost() + ")");
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class bookmystay {

    public static void main(String[] args) {

        // Simulated Reservation IDs (from Use Case 6)
        String reservation1 = "SI-123ABC";
        String reservation2 = "SU-456XYZ";

        // Step 1: Create Add-On Services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa Access", 1500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800);

        // Step 2: Add-On Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Step 3: Guest selects services
        manager.addServices(reservation1, Arrays.asList(breakfast, spa));
        manager.addServices(reservation2, Arrays.asList(airportPickup));

        // Step 4: Display services + cost
        manager.displayServices(reservation1);
        manager.displayServices(reservation2);

        // NOTE:
        // No inventory or booking logic is touched here
    }
}