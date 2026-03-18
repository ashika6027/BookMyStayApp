import java.util.*;

/**
 * Reservation represents a booking request.
 *
 * @author Student
 * @version 6.0
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/**
 * RoomInventory manages room availability.
 *
 * @version 6.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int count = inventory.get(roomType);
        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}


/**
 * BookingService processes booking requests and performs room allocation.
 *
 * @version 6.0
 */
class BookingService {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    // Set to track all allocated room IDs
    private Set<String> allocatedRoomIds;

    // Map room type -> assigned room IDs
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {

        this.inventory = inventory;

        bookingQueue = new LinkedList<>();
        allocatedRoomIds = new HashSet<>();
        allocatedRooms = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        bookingQueue.add(r);
    }

    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.poll();

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            System.out.println("\nProcessing request for " + guest);

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Store room ID
                allocatedRoomIds.add(roomId);

                allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.decrementRoom(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest : " + guest);
                System.out.println("Room Type : " + roomType);
                System.out.println("Room ID : " + roomId);

            } else {

                System.out.println("Reservation Failed - No rooms available for " + roomType);
            }
        }
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
        String roomId;

        do {
            int number = new Random().nextInt(900) + 100;
            roomId = prefix + number;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    public void displayAllocatedRooms() {

        System.out.println("\nAllocated Rooms:");

        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {

            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}


/**
 * UseCase6RoomAllocationService
 * Demonstrates reservation confirmation and room allocation.
 *
 * @version 6.1
 */
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println(" Book My Stay - Room Allocation Service ");
        System.out.println(" Version 6.1");
        System.out.println("=========================================");

        RoomInventory inventory = new RoomInventory();

        BookingService bookingService = new BookingService(inventory);

        // Booking requests (FIFO queue)
        bookingService.addReservation(new Reservation("Alice", "Single Room"));
        bookingService.addReservation(new Reservation("Bob", "Double Room"));
        bookingService.addReservation(new Reservation("Charlie", "Single Room"));
        bookingService.addReservation(new Reservation("David", "Suite Room"));
        bookingService.addReservation(new Reservation("Eve", "Suite Room")); // May fail

        // Process bookings
        bookingService.processBookings();

        // Display results
        bookingService.displayAllocatedRooms();

        inventory.displayInventory();
    }
}