import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Custom Exception for Booking Errors
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking Request class
class BookingRequest {
    String bookingID;
    String roomType;
    int roomsRequested;

    public BookingRequest(String bookingID, String roomType, int roomsRequested) {
        this.bookingID = bookingID;
        this.roomType = roomType;
        this.roomsRequested = roomsRequested;
    }
}

// Thread-safe Booking Processor
class BookingProcessor implements Runnable {

    private final BlockingQueue<BookingRequest> queue;
    private final BookMyStay hotel;

    public BookingProcessor(BlockingQueue<BookingRequest> queue, BookMyStay hotel) {
        this.queue = queue;
        this.hotel = hotel;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            try {
                BookingRequest request = queue.take(); // retrieve request
                hotel.bookRoom(request.bookingID, request.roomType, request.roomsRequested);
            } catch (InvalidBookingException e) {
                System.out.println("Booking Failed: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class BookMyStay {

    // Shared room inventory
    private final Map<String, Integer> roomInventory = new HashMap<>();
    private final Map<String, String> bookingHistory = new HashMap<>();

    public BookMyStay() {
        roomInventory.put("STANDARD", 5);
        roomInventory.put("DELUXE", 3);
        roomInventory.put("SUITE", 2);
    }

    // Thread-safe booking
    public void bookRoom(String bookingID, String roomType, int roomsRequested) throws InvalidBookingException {
        synchronized (this) { // critical section
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

            // Update inventory
            roomInventory.put(roomType, available - roomsRequested);
            bookingHistory.put(bookingID, roomType);

            System.out.println("Booking Successful! ID: " + bookingID + ", Room: " + roomType +
                    ", Remaining: " + roomInventory.get(roomType));
        }
    }

    // Main method to simulate concurrent bookings
    public static void main(String[] args) throws InterruptedException {

        BookMyStay hotel = new BookMyStay();
        BlockingQueue<BookingRequest> queue = new LinkedBlockingQueue<>();

        // Simulate multiple guests submitting booking requests concurrently
        queue.add(new BookingRequest("B001", "STANDARD", 2));
        queue.add(new BookingRequest("B002", "DELUXE", 1));
        queue.add(new BookingRequest("B003", "SUITE", 1));
        queue.add(new BookingRequest("B004", "STANDARD", 3));
        queue.add(new BookingRequest("B005", "DELUXE", 2)); // this may fail if insufficient

        // Create multiple threads to process bookings
        Thread t1 = new Thread(new BookingProcessor(queue, hotel));
        Thread t2 = new Thread(new BookingProcessor(queue, hotel));
        Thread t3 = new Thread(new BookingProcessor(queue, hotel));

        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Final Room Inventory: " + hotel.roomInventory);
        System.out.println("All bookings processed safely under concurrency.");
    }
}