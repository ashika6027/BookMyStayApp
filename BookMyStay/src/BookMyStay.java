import java.util.*;

/**
 * Reservation represents a confirmed booking.
 *
 * @version 8.0
 */
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("-----------------------------");
    }
}


/**
 * BookingHistory stores confirmed reservations.
 *
 * @version 8.0
 */
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve reservation list
    public List<Reservation> getReservations() {
        return reservations;
    }
}


/**
 * BookingReportService generates reports
 * using stored booking history.
 *
 * @version 8.0
 */
class BookingReportService {

    public void displayAllBookings(List<Reservation> reservations) {

        System.out.println("\n===== Booking History =====");

        for (Reservation r : reservations) {
            r.displayReservation();
        }
    }

    public void generateSummaryReport(List<Reservation> reservations) {

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {

            String roomType = r.getRoomType();

            roomTypeCount.put(
                    roomType,
                    roomTypeCount.getOrDefault(roomType, 0) + 1
            );
        }

        System.out.println("\n===== Booking Summary Report =====");

        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {

            System.out.println(entry.getKey() + " Bookings : " + entry.getValue());
        }
    }
}


/**
 * UseCase8BookingHistoryReport
 * Demonstrates booking history tracking
 * and reporting functionality.
 *
 * @version 8.1
 */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println(" Book My Stay - Booking History Report ");
        System.out.println(" Version 8.1");
        System.out.println("=======================================");

        BookingHistory history = new BookingHistory();

        // Simulated confirmed reservations
        history.addReservation(new Reservation("RES101", "Alice", "Single Room"));
        history.addReservation(new Reservation("RES102", "Bob", "Double Room"));
        history.addReservation(new Reservation("RES103", "Charlie", "Suite Room"));
        history.addReservation(new Reservation("RES104", "David", "Single Room"));

        BookingReportService reportService = new BookingReportService();

        // Admin views booking history
        reportService.displayAllBookings(history.getReservations());

        // Admin generates summary report
        reportService.generateSummaryReport(history.getReservations());

        System.out.println("\nReport generation completed.");
    }
}