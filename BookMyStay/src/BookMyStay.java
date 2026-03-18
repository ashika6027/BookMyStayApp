import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

// Custom Exception for Invalid Booking or Cancellation
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

public class BookMyStay {

    // Room inventory
    private static Map<String, Integer> roomInventory = new HashMap<>();

    // Booking history: bookingID -> roomType
    private static Map<String, String> bookingHistory = new HashMap<>();

    // Stack to track released room IDs for rollback
    private static Stack<String> rollbackStack = new Stack<>();

    // Initialize room availability
    static {
        roomInventory.put("STANDARD", 5);
        roomInventory.put("DELUXE", 3);
        roomInventory.put("SUITE", 2);
    }

    // Validate booking before creation
    public static void validateBooking(String roomType, int roomsRequested) throws InvalidBookingException {
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid Room Type: " + roomType);
        }
        if (roomsRequested <= 0) {
            throw new InvalidBookingException("Number of rooms must be greater than 0.");
        }
        int availableRooms = roomInventory.get(roomType);
        if (roomsRequested > availableRooms) {
            throw new InvalidBookingException("Not enough rooms available. Available: " + availableRooms);
        }
    }

    // Create booking
    public static void bookRoom(String bookingID, String roomType, int roomsRequested) throws InvalidBookingException {
        validateBooking(roomType, roomsRequested);

        int remaining = roomInventory.get(roomType) - roomsRequested;
        if (remaining < 0) {
            throw new InvalidBookingException("Booking would cause negative inventory.");
        }
        roomInventory.put(roomType, remaining);

        // Save booking history
        bookingHistory.put(bookingID, roomType);

        System.out.println("Booking Successful! Booking ID: " + bookingID);
        System.out.println("Remaining " + roomType + " rooms: " + remaining);
    }

    // Cancel booking
    public static void cancelBooking(String bookingID) throws InvalidBookingException {
        // Validate that booking exists
        if (!bookingHistory.containsKey(bookingID)) {
            throw new InvalidBookingException("Booking ID does not exist or already cancelled: " + bookingID);
        }

        String roomType = bookingHistory.get(bookingID);

        // Push to rollback stack (LIFO)
        rollbackStack.push(bookingID);

        // Restore inventory
        roomInventory.put(roomType, roomInventory.get(roomType) + 1);

        // Remove from booking history
        bookingHistory.remove(bookingID);

        System.out.println("Cancellation Successful! Booking ID: " + bookingID);
        System.out.println("Restored " + roomType + " room. Available now: " + roomInventory.get(roomType));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("=== Book My Stay ===");
            System.out.println("1. Book Room");
            System.out.println("2. Cancel Booking");
            System.out.print("Choose an option (1/2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                System.out.print("Enter Booking ID: ");
                String bookingID = scanner.nextLine();

                System.out.print("Enter Room Type (STANDARD / DELUXE / SUITE): ");
                String roomType = scanner.nextLine().toUpperCase();

                System.out.print("Enter Number of Rooms: ");
                int rooms = scanner.nextInt();

                bookRoom(bookingID, roomType, rooms);

            } else if (choice == 2) {
                System.out.print("Enter Booking ID to Cancel: ");
                String bookingID = scanner.nextLine();
                cancelBooking(bookingID);

            } else {
                System.out.println("Invalid option.");
            }

        } catch (InvalidBookingException e) {
            System.out.println("Operation Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter correct values.");
        }

        System.out.println("System is still running safely.");
        scanner.close();
    }
}