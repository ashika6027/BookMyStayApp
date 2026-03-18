import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Custom Exception for Booking Errors
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Serializable class to hold system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> roomInventory;
    Map<String, String> bookingHistory;

    public SystemState(Map<String, Integer> inventory, Map<String, String> history) {
        this.roomInventory = inventory;
        this.bookingHistory = history;
    }
}

public class BookMyStay {

    private static final String DATA_FILE = "bookmystay_state.ser";

    private Map<String, Integer> roomInventory = new HashMap<>();
    private Map<String, String> bookingHistory = new HashMap<>();

    public BookMyStay() {
        // Initialize inventory if no saved state
        roomInventory.put("STANDARD", 5);
        roomInventory.put("DELUXE", 3);
        roomInventory.put("SUITE", 2);
    }

    // Thread-safe booking
    public synchronized void bookRoom(String bookingID, String roomType, int roomsRequested)
            throws InvalidBookingException {

        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = roomInventory.get(roomType);
        if (roomsRequested <= 0) {
            throw new InvalidBookingException("Number of rooms must be > 0");
        }
        if (roomsRequested > available) {
            throw new InvalidBookingException(
                    "Not enough rooms available for " + roomType + ". Available: " + available);
        }

        roomInventory.put(roomType, available - roomsRequested);
        bookingHistory.put(bookingID, roomType);

        System.out.println("Booking Successful! ID: " + bookingID + ", Room: " + roomType +
                ", Remaining: " + roomInventory.get(roomType));
    }

    // Save system state to file
    public synchronized void saveState() {
        SystemState state = new SystemState(roomInventory, bookingHistory);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load system state from file
    public synchronized void loadState() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No previous state found. Starting with default inventory.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            SystemState state = (SystemState) ois.readObject();
            this.roomInventory = state.roomInventory;
            this.bookingHistory = state.bookingHistory;
            System.out.println("System state restored successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with default inventory. " + e.getMessage());
        }
    }

    // Display current state
    public void displayState() {
        System.out.println("Current Room Inventory: " + roomInventory);
        System.out.println("Booking History: " + bookingHistory);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookMyStay hotel = new BookMyStay();

        // Load state at startup
        hotel.loadState();

        boolean running = true;
        while (running) {
            System.out.println("\n=== Book My Stay ===");
            System.out.println("1. Book Room");
            System.out.println("2. View State");
            System.out.println("3. Save & Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    try {
                        System.out.print("Enter Booking ID: ");
                        String bookingID = scanner.nextLine();

                        System.out.print("Enter Room Type (STANDARD / DELUXE / SUITE): ");
                        String roomType = scanner.nextLine().toUpperCase();

                        System.out.print("Enter Number of Rooms: ");
                        int rooms = Integer.parseInt(scanner.nextLine());

                        hotel.bookRoom(bookingID, roomType, rooms);
                    } catch (InvalidBookingException e) {
                        System.out.println("Booking Failed: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number input.");
                    }
                    break;

                case "2":
                    hotel.displayState();
                    break;

                case "3":
                    hotel.saveState();
                    running = false;
                    System.out.println("Exiting system.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}