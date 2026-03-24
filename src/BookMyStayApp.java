import java.util.*;

// ================== MAIN ==================
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory(5, 3, 2);
        BookingHistory history = new BookingHistory();
        RoomAllocationService allocation = new RoomAllocationService();
        CancellationService cancelService = new CancellationService();

        // Simulate bookings
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");

        String id1 = allocation.allocateRoom(r1, inventory);
        String id2 = allocation.allocateRoom(r2, inventory);

        history.addReservation(id1, r1);
        history.addReservation(id2, r2);

        // 🔥 UC10: Cancellation
        System.out.println("\nBooking Cancellation\n");

        cancelService.cancelBooking(id1, history, inventory);

        // Show rollback history
        cancelService.printRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getRoomAvailability().get("Single"));
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

    public void increaseRoom(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public boolean reduceRoom(String type) {
        int count = availability.getOrDefault(type, 0);
        if (count > 0) {
            availability.put(type, count - 1);
            return true;
        }
        return false;
    }
}

// ================== ALLOCATION ==================
class RoomAllocationService {

    private Map<String, Integer> counters = new HashMap<>();

    public String allocateRoom(Reservation r, RoomInventory inv) {

        String type = r.getRoomType();

        if (!inv.reduceRoom(type)) {
            System.out.println("Booking failed for " + r.getGuestName());
            return null;
        }

        int count = counters.getOrDefault(type, 0) + 1;
        counters.put(type, count);

        String roomId = type + "-" + count;

        System.out.println("Booking confirmed for Guest: "
                + r.getGuestName() + ", Room ID: " + roomId);

        return roomId;
    }
}

// ================== HISTORY ==================
class BookingHistory {

    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(String id, Reservation r) {
        bookings.put(id, r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }

    public boolean exists(String id) {
        return bookings.containsKey(id);
    }
}

// ================== CANCELLATION ==================
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) {

        if (!history.exists(reservationId)) {
            System.out.println("Cancellation failed: Invalid reservation ID");
            return;
        }

        Reservation r = history.getReservation(reservationId);
        String roomType = r.getRoomType();

        // 🔥 rollback step
        rollbackStack.push(reservationId);

        // restore inventory
        inventory.increaseRoom(roomType);

        // remove booking
        history.removeReservation(reservationId);

        System.out.println("Booking cancelled successfully. "
                + "Inventory restored for room type: " + roomType);
    }

    public void printRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");

        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + rollbackStack.get(i));
        }
    }
}