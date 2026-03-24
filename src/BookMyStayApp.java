import java.util.*;

// ================== MAIN CLASS ==================
public class BookMyStayApp {

    public static void main(String[] args) {

        // UC4: Search
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory(5, 3, 2);
        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(inventory, single, doubleRoom, suite);

        // UC5: Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Double"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // UC6: Allocation
        RoomAllocationService allocationService = new RoomAllocationService();

        // UC8: Booking History
        BookingHistory history = new BookingHistory();

        System.out.println("\nRoom Allocation Processing\n");

        while (queue.hasPendingRequests()) {
            Reservation r = queue.getNextRequest();

            boolean success = allocationService.allocateRoom(r, inventory);

            if (success) {
                history.addReservation(r); // 🔥 store history
            }
        }

        // UC8: Reporting
        System.out.println("\nBooking History and Reporting\n");

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}

// ================== ROOM ==================
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int beds, int size, double price) {
        this.numberOfBeds = beds;
        this.squareFeet = size;
        this.pricePerNight = price;
    }

    public abstract void displayRoomDetails();
}

class SingleRoom extends Room {
    public SingleRoom() { super(1, 250, 1500.0); }

    public void displayRoomDetails() {
        System.out.println("Single Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super(2, 400, 2500.0); }

    public void displayRoomDetails() {
        System.out.println("Double Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 750, 5000.0); }

    public void displayRoomDetails() {
        System.out.println("Suite Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
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

    public boolean reduceRoom(String type) {
        int count = availability.getOrDefault(type, 0);
        if (count > 0) {
            availability.put(type, count - 1);
            return true;
        }
        return false;
    }
}

// ================== SEARCH ==================
class RoomSearchService {
    public void searchAvailableRooms(RoomInventory inv, Room s, Room d, Room su) {
        Map<String, Integer> a = inv.getRoomAvailability();

        System.out.println("Room Search\n");

        if (a.get("Single") > 0) {
            s.displayRoomDetails();
            System.out.println("Available: " + a.get("Single") + "\n");
        }

        if (a.get("Double") > 0) {
            d.displayRoomDetails();
            System.out.println("Available: " + a.get("Double") + "\n");
        }

        if (a.get("Suite") > 0) {
            su.displayRoomDetails();
            System.out.println("Available: " + a.get("Suite") + "\n");
        }
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
    private Queue<Reservation> q = new LinkedList<>();

    public void addRequest(Reservation r) { q.offer(r); }
    public Reservation getNextRequest() { return q.poll(); }
    public boolean hasPendingRequests() { return !q.isEmpty(); }
}

// ================== ALLOCATION ==================
class RoomAllocationService {

    private Map<String, Set<String>> map = new HashMap<>();

    public boolean allocateRoom(Reservation r, RoomInventory inv) {

        String type = r.getRoomType();
        int available = inv.getRoomAvailability().getOrDefault(type, 0);

        if (available <= 0) {
            System.out.println("Booking failed for " + r.getGuestName());
            return false;
        }

        int count = map.getOrDefault(type, new HashSet<>()).size() + 1;
        String id = type + "-" + count;

        map.computeIfAbsent(type, k -> new HashSet<>()).add(id);
        inv.reduceRoom(type);

        System.out.println("Booking confirmed for Guest: "
                + r.getGuestName() + ", Room ID: " + id);

        return true;
    }
}

// ================== HISTORY ==================
class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        confirmedReservations.add(r);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// ================== REPORT ==================
class BookingReportService {

    public void generateReport(BookingHistory history) {

        System.out.println("Booking History Report\n");

        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println("Guest: " + r.getGuestName()
                    + ", Room Type: " + r.getRoomType());
        }
    }
}