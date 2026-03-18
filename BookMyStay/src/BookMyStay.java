import java.util.LinkedList;
import java.util.Queue;

/**
 * Reservation class represents a guest booking request.
 *
 * @author Student
 * @version 5.0
 */
class Reservation {

    private String guestName;
    private String roomType;

    // Constructor
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    // Display reservation request
    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}


/**
 * BookingRequestQueue manages incoming booking requests
 * using FIFO queue structure.
 *
 * @author Student
 * @version 5.0
 */
class BookingRequestQueue {

    private Queue<Reservation> bookingQueue;

    // Constructor
    public BookingRequestQueue() {
        bookingQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        bookingQueue.add(reservation);
        System.out.println("Booking request added to queue.");
    }

    // Display all queued requests
    public void displayQueue() {

        System.out.println("\nCurrent Booking Requests (FIFO Order):");

        for (Reservation r : bookingQueue) {
            r.displayReservation();
            System.out.println("----------------------");
        }
    }
}


/**
 * UseCase5BookingRequestQueue
 *
 * Demonstrates booking request intake using Queue
 * following First-Come-First-Served principle.
 *
 * @author Student
 * @version 5.1
 */
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay - Booking Request Queue ");
        System.out.println(" Version 5.1");
        System.out.println("=======================================");

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue
        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        // Display queued requests
        queue.displayQueue();

        System.out.println("\nAll requests stored in arrival order.");
        System.out.println("No room allocation performed yet.");
    }
}