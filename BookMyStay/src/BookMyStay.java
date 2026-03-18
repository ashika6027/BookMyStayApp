import java.util.*;

/**
 * AddOnService represents an optional service
 * that can be attached to a reservation.
 *
 * @version 7.0
 */
class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void displayService() {
        System.out.println(serviceName + " - $" + price);
    }
}


/**
 * AddOnServiceManager manages services
 * associated with reservations.
 *
 * @version 7.0
 */
class AddOnServiceManager {

    // Map reservationId -> List of services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach service to reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added: " + service.getServiceName()
                + " for Reservation " + reservationId);
    }

    // Display services for reservation
    public void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected for reservation " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation: " + reservationId);

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    // Calculate total service cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        double total = 0;

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getPrice();
            }
        }

        return total;
    }
}


/**
 * UseCase7AddOnServiceSelection
 * Demonstrates attaching optional services to reservations.
 *
 * @version 7.1
 */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay - Add-On Service System ");
        System.out.println(" Version 7.1");
        System.out.println("=======================================");

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example reservation IDs (from previous booking stage)
        String reservation1 = "RES101";
        String reservation2 = "RES102";

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 15);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 30);
        AddOnService spaAccess = new AddOnService("Spa Access", 50);

        // Guest selects services
        manager.addService(reservation1, breakfast);
        manager.addService(reservation1, spaAccess);
        manager.addService(reservation2, airportPickup);

        // Display services
        manager.displayServices(reservation1);
        manager.displayServices(reservation2);

        // Calculate total cost
        System.out.println("\nTotal Add-On Cost for " + reservation1 + " : $" +
                manager.calculateTotalCost(reservation1));

        System.out.println("Total Add-On Cost for " + reservation2 + " : $" +
                manager.calculateTotalCost(reservation2));

        System.out.println("\nAdd-on services processed successfully.");
        System.out.println("Core booking and inventory remain unchanged.");
    }
}