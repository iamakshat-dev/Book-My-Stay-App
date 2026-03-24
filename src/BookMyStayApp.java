import java.util.*;

// ================== MAIN CLASS ==================
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Validation\n");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory(5, 3, 2);
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue queue = new BookingRequestQueue();

        try {
            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String type = scanner.nextLine();

            // 🔥 VALIDATION STEP
            validator.validate(name, type, inventory);

            // If valid → proceed
            queue.addRequest(new Reservation(name, type));

            System.out.println("\nBooking accepted!");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

// ================== CUSTOM EXCEPTION ==================
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ================== VALIDATOR ==================
class ReservationValidator {

    public void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!roomType.equals("Single") &&
                !roomType.equals("Double") &&
                !roomType.equals("Suite")) {

            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate availability
        int available = inventory.getRoomAvailability()
                .getOrDefault(roomType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

// ================== INVENTORY ==================
class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory(int s, int d, int su) {
        availability = new HashMap<>();
        availability.put("Single", s);
        availability.put("Double", d);
        availability.put("Suite", su);
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }
}

// ================== RESERVATION ==================
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String g, String r) {
        guestName = g;
        roomType = r;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// ================== QUEUE ==================
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}