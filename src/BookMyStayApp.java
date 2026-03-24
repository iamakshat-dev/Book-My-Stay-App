import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    public static void main(String[] args) {

        // Room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Centralized Inventory
        RoomInventory inventory = new RoomInventory(5, 3, 2);

        // Search Service (Read-only)
        RoomSearchService searchService = new RoomSearchService();

        // Perform Room Search
        searchService.searchAvailableRooms(
                inventory,
                single,
                doubleRoom,
                suite
        );
    }
}

// ================== ABSTRACT ROOM ==================
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public abstract void displayRoomDetails();
}

// ================== SINGLE ROOM ==================
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 250, 1500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Single Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

// ================== DOUBLE ROOM ==================
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 400, 2500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Double Room:");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

// ================== SUITE ROOM ==================
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 750, 5000.0);
    }

    @Override
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

    // Read-only access
    public Map<String, Integer> getRoomAvailability() {
        return availability;
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