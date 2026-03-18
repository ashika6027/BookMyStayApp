import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory
 *
 * This class manages the centralized inventory of rooms
 * using a HashMap data structure.
 *
 * @author Student
 * @version 3.0
 */
class RoomInventory {

    // HashMap to store room type and available count
    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {

        inventory = new HashMap<>();

        // Initialize room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Method to display inventory
    public void displayInventory() {

        System.out.println("\nCurrent Room Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}


/**
 * UseCase3InventorySetup
 *
 * Demonstrates centralized inventory management
 * for the Book My Stay Hotel Booking System.
 *
 * @author Student
 * @version 3.1
 */
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay - Hotel Booking System ");
        System.out.println(" Version 3.1");
        System.out.println("=======================================");

        // Initialize inventory system
        RoomInventory inventory = new RoomInventory();

        // Display current inventory
        inventory.displayInventory();

        // Example availability lookup
        System.out.println("\nChecking availability for Single Room...");
        System.out.println("Available: " + inventory.getAvailability("Single Room"));

        // Example update
        System.out.println("\nUpdating availability for Single Room...");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nInventory management demonstration completed.");
    }
}
