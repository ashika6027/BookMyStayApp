import java.util.HashMap;

/**
 * Abstract Room class representing common room properties.
 */
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : $" + price);
    }
}


/* Concrete Room Classes */

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 350);
    }
}


/**
 * Inventory class that stores room availability.
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}


/**
 * Main application class for Use Case 4.
 *
 * Demonstrates room search functionality.
 *
 * @version 4.0
 */
public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" Book My Stay - Room Search ");
        System.out.println("=================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Search logic (read-only)
        searchRoom(single, inventory);
        searchRoom(doubleRoom, inventory);
        searchRoom(suite, inventory);

        System.out.println("\nSearch completed. Inventory state unchanged.");
    }

    // Search method (read-only operation)
    public static void searchRoom(Room room, RoomInventory inventory) {

        int availability = inventory.getAvailability(room.roomType);

        if (availability > 0) {

            System.out.println("\nAvailable Room:");
            room.displayDetails();
            System.out.println("Available Count : " + availability);
        }
    }
}