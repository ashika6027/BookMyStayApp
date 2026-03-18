import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

public class BookMyStay {

    // Room inventory
    private static Map<String, Integer> roomInventory = new HashMap<>();

    // Initialize room availability
    static {
        roomInventory.put("STANDARD", 5);
        roomInventory.put("DELUXE", 3);
        roomInventory.put("SUITE", 2);
    }

    // Method to validate booking
    public static void validateBooking(String roomType, int roomsRequested) throws InvalidBookingException {

        // Validate room type
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid Room Type: " + roomType);
        }

        // Validate number of rooms
        if (roomsRequested <= 0) {
            throw new InvalidBookingException("Number of rooms must be greater than 0.");
        }

        int availableRooms = roomInventory.get(roomType);

        // Prevent negative inventory
        if (roomsRequested > availableRooms) {
            throw new InvalidBookingException("Not enough rooms available. Available: " + availableRooms);
        }
    }

    // Booking method
    public static void bookRoom(String roomType, int roomsRequested) throws InvalidBookingException {

        // Fail-fast validation
        validateBooking(roomType, roomsRequested);

        int remaining = roomInventory.get(roomType) - roomsRequested;

        // Guarding system state
        if (remaining < 0) {
            throw new InvalidBookingException("Booking would cause negative inventory.");
        }

        roomInventory.put(roomType, remaining);

        System.out.println("Booking Successful!");
        System.out.println("Remaining " + roomType + " rooms: " + remaining);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("=== Book My Stay ===");

            System.out.print("Enter Room Type (STANDARD / DELUXE / SUITE): ");
            String roomType = scanner.nextLine().toUpperCase();

            System.out.print("Enter Number of Rooms: ");
            int rooms = scanner.nextInt();

            // Process booking
            bookRoom(roomType, rooms);

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter correct values.");
        }

        System.out.println("System is still running safely.");

        scanner.close();
    }
}