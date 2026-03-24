import java.util.*;

// ================== MAIN CLASS ==================
public class BookMyStayApp {

    public static void main(String[] args) {

        // ================== UC4 (SEARCH) ==================
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory(5, 3, 2);

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(
                inventory,
                single,
                doubleRoom,
                suite
        );

        // ================== UC5 (BOOKING QUEUE) ==================
        System.out.println("Booking Request Queue\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Add to queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Process in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation r = bookingQueue.getNextRequest();

            System.out.println(
                    "Processing booking for Guest: " +
                            r.getGuestName() +
                            ", Room Type: " +
                            r.getRoomType()
            );
        }
    }
}

// ================== ABSTRACT ROOM ==================
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

// ================== ROOM TYPES ==================
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }

    public void displayRoomDetails() {
        System.out.println("Single Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }

    public void displayRoomDetails() {
        System.out.println("Double Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }

    public void displayRoomDetails() {
        System.out.println("Suite Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

// ================== ROOM INVENTORY ==================
class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory(int single, int dbl, int suite) {
        availability = new HashMap<>();
        availability.put("Single", single);
        availability.put("Double", dbl);
        availability.put("Suite", suite);
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }

    // UC5 NOT USED YET (for future UC6)
    public boolean reduceRoom(String type) {
        int count = availability.getOrDefault(type, 0);
        if (count > 0) {
            availability.put(type, count - 1);
            return true;
        }
        return false;
    }
}

// ================== SEARCH SERVICE ==================
class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Room Search\n");

        if (availability.getOrDefault("Single", 0) > 0) {
            singleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Single"));
            System.out.println();
        }

        if (availability.getOrDefault("Double", 0) > 0) {
            doubleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Double"));
            System.out.println();
        }

        if (availability.getOrDefault("Suite", 0) > 0) {
            suiteRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Suite"));
            System.out.println();
        }
    }
}

// ================== RESERVATION ==================
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

// ================== BOOKING QUEUE ==================
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}